/* license: https://mit-license.org
 *
 *  DIMP : Decentralized Instant Messaging Protocol
 *
 *                                Written in 2019 by Moky <albert.moky@gmail.com>
 *
 * ==============================================================================
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Albert Moky
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
package chat.dim.dkd;

import java.util.Map;

import chat.dim.ext.CommandHandler;
import chat.dim.ext.SharedCommandExtensions;
import chat.dim.protocol.Command;
import chat.dim.protocol.ContentType;


public class BaseCommand extends BaseContent implements Command {

    /**
     * Create command with a raw map.
     *
     * @param content - raw command map.
     */
    public BaseCommand(Map<String, Object> content) {
        super(content);
    }

    /**
     * Create command with the given message type and command name.
     *
     * @param type - message type
     * @param cmd  - command name
     */
    public BaseCommand(String type, String cmd) {
        super(type);
        put("command", cmd);
    }

    /**
     * Create command with the given command name.
     *
     * The message type will be set to {@link ContentType#COMMAND} automatically.
     *
     * @param cmd - command name
     */
    public BaseCommand(String cmd) {
        this(ContentType.COMMAND, cmd);
    }

    @Override
    public String getCmd() {
        CommandHandler helper = SharedCommandExtensions.handler;
        return helper.getCmd(toMap(), "");
        // return getString("command", "");
    }

}
