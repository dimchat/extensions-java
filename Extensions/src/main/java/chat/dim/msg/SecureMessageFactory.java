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
package chat.dim.msg;

import java.util.HashMap;
import java.util.Map;

import chat.dim.dkd.EncryptedBundle;
import chat.dim.ext.MessageHandler;
import chat.dim.ext.SharedMessageExtensions;
import chat.dim.format.PlainData;
import chat.dim.protocol.ID;
import chat.dim.protocol.InstantMessage;
import chat.dim.protocol.SecureMessage;
import chat.dim.protocol.TransportableData;

/**
 *  SecureMessage Factory
 */
public class SecureMessageFactory implements SecureMessage.Factory {

    @Override
    public SecureMessage createSecureMessage(InstantMessage iMsg, byte[] ciphertext, Map<ID, EncryptedBundle> keyBundles) {
        MessageHandler helper = SharedMessageExtensions.handler;
        //
        //  1. encode ciphertext
        //
        TransportableData encodedData;
        if (helper.isBroadcast(iMsg)) {
            // broadcast message content will not be encrypted (just encoded to JsON),
            // so no need to encode to Base64 here
            encodedData = PlainData.create(ciphertext);  // UTF8.decode(ciphertext);
        } else {
            // message content had been encrypted by a symmetric key,
            // so the data should be encoded here (with algorithm 'base64' as default).
            encodedData = TransportableData.create(ciphertext);
        }
        assert !encodedData.isEmpty() : "failed to encode content data: " + ciphertext.length + " byte(s)";
        //
        //  2. encode keys
        //
        Map<String, Object> msgKeys;
        if (keyBundles == null) {
            msgKeys = null;
        } else {
            msgKeys = new HashMap<>();
            assert !helper.isBroadcast(iMsg) : "broadcast message should not contains keys: " + iMsg;
            // message key had been encrypted by a public key,
            // so the data should be encoded here (with algorithm 'base64' as default).
            ID receiver;
            EncryptedBundle bundle;
            Map<String, Object> encodedKeys;
            for (Map.Entry<ID, EncryptedBundle> entry : keyBundles.entrySet()) {
                receiver = entry.getKey();
                bundle = entry.getValue();
                encodedKeys = bundle.encode(receiver);
                // TODO: check for wildcard
                if (encodedKeys == null || encodedKeys.isEmpty()) {
                    assert false : "failed to encode key data: " + receiver;
                    continue;
                }
                // insert to 'message.keys' with ID + terminal
                msgKeys.putAll(encodedKeys);
            }
            // TODO: put key digest
        }
        //
        //  3. create message
        //
        Map<String, Object> info = iMsg.copyMap(false);
        // replace 'content' with encrypted and encoded 'data'
        info.remove("content");
        info.put("data", encodedData.serialize());
        // insert as 'keys'
        if (msgKeys != null && !msgKeys.isEmpty()) {
            info.put("keys", msgKeys);
        }
        return new EncryptedMessage(info);
    }

    @Override
    public SecureMessage parseSecureMessage(Map<String, Object> msg) {
        // check 'sender', 'data'
        if (!msg.containsKey("sender") || !msg.containsKey("data")) {
            // msg.sender should not be empty
            // msg.data should not be empty
            assert false : "message error: " + msg;
            return null;
        }
        // check 'signature'
        if (msg.containsKey("signature")) {
            return new NetworkMessage(msg);
        }
        return new EncryptedMessage(msg);
    }

}
