/* license: https://mit-license.org
 *
 *  DIMP : Decentralized Instant Messaging Protocol
 *
 *                                Written in 2022 by Moky <albert.moky@gmail.com>
 *
 * ==============================================================================
 * The MIT License (MIT)
 *
 * Copyright (c) 2022 Albert Moky
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * ==============================================================================
 */
package chat.dim.ext;

import java.util.HashMap;
import java.util.Map;

import chat.dim.data.Converter;
import chat.dim.data.Wrapper;
import chat.dim.dkd.cmd.BaseReceiptCommand;
import chat.dim.protocol.Command;
import chat.dim.protocol.Content;
import chat.dim.protocol.Envelope;
import chat.dim.protocol.ID;
import chat.dim.protocol.ReceiptCommand;

/**
 *  Command GeneralHelper
 */
public class GeneralCommandHelper implements CommandHandler, CommandHelper {

    private final Map<String, Command.Factory> commandFactories = new HashMap<>();

    @Override
    public String getCmd(Map<?, ?> content, String defaultValue) {
        return Converter.getString(content.get("command"), defaultValue);
    }

    @Override
    public Command createReceipt(String text, Envelope head, Content body) {
        Map<String, Object> origin;
        if (head == null) {
            origin = null;
        } else {
            origin = head.copyMap(false);
            if (origin.containsKey("data")) {
                origin.remove("data");
                origin.remove("keys");
                origin.remove("meta");
                origin.remove("visa");
            }
            if (body != null) {
                long sn = body.getSerialNumber();
                origin.put("sn", sn);
            }
        }
        ReceiptCommand content = new BaseReceiptCommand(text, origin);
        if (body != null) {
            // check group
            ID group = body.getGroup();
            if (group != null) {
                content.setGroup(group);
            }
        }
        return content;
    }

    /// Get a mutable map from an object.
    ///
    /// [dict] is a raw map or a mapping instance;
    /// returns null if it cannot be converted.
    // protected
    protected Map<String, Object> getMap(Object dict) {
        return Wrapper.getMap(dict);
    }

    //
    //  Command Helper
    //

    @Override
    public void setCommandFactory(String cmd, Command.Factory factory) {
        commandFactories.put(cmd, factory);
    }

    @Override
    public Command.Factory getCommandFactory(String cmd) {
        return commandFactories.get(cmd);
    }

    @Override
    public Command parseCommand(Object content) {
        if (content == null) {
            return null;
        } else if (content instanceof Command) {
            return (Command) content;
        }
        Map<String, Object> info = getMap(content);
        if (info == null) {
            assert false : "command error: " + content;
            return null;
        }
        // get factory by command name
        String name = getCmd(info, null);
        // assert name != null : "command name error: " + content;
        Command.Factory factory = name == null ? null : getCommandFactory(name);
        if (factory == null) {
            // unknown command name, get base command factory
            factory = getDefaultFactory(info);
            if (factory == null) {
                assert false : "cannot parse command: " + content;
                return null;
            }
        }
        return factory.parseCommand(info);
    }

    private static Command.Factory getDefaultFactory(Map<?, ?> info) {
        MessageHandler handler = SharedMessageExtensions.handler;
        ContentHelper helper = SharedMessageExtensions.contentHelper;
        // get factory by content type
        String type = handler.getContentType(info, null);
        if (type != null) {
            Content.Factory factory = helper.getContentFactory(type);
            if (factory instanceof Command.Factory) {
                return (Command.Factory) factory;
            }
        }
        assert false : "cannot parse command: " + info;
        return null;
    }

}
