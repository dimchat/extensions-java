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

import java.util.Map;

import chat.dim.protocol.ReliableMessage;
import chat.dim.protocol.SecureMessage;
import chat.dim.protocol.TransportableData;

/**
 *  ReliableMessage Factory
 */
public class ReliableMessageFactory implements ReliableMessage.Factory {

    @Override
    public ReliableMessage createReliableMessage(SecureMessage sMsg, byte[] signature) {
        //
        //  1. encode signature
        //
        TransportableData base64 = TransportableData.create(signature);
        assert !base64.isEmpty() : "failed to encode signature: " + signature.length + " byte(s) "
                + sMsg.getSender() + " => " + sMsg.getReceiver() + ", " + sMsg.getGroup();
        //
        //  2. create message
        //
        Map<String, Object> info = sMsg.copyMap(false);
        info.put("signature", base64.serialize());
        return new NetworkMessage(info);
    }

    @Override
    public ReliableMessage parseReliableMessage(Map<String, Object> msg) {
        // check 'sender', 'data', 'signature'
        if (msg.get("sender") == null || msg.get("data") == null || msg.get("signature") == null) {
            // msg.sender should not be empty
            // msg.data should not be empty
            // msg.signature should not be empty
            assert false : "message error: " + msg;
            return null;
        }
        return new NetworkMessage(msg);
    }

}
