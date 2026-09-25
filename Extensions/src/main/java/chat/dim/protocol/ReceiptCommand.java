/* license: https://mit-license.org
 *
 *  DIMP : Decentralized Instant Messaging Protocol
 *
 *                                Written in 2023 by Moky <albert.moky@gmail.com>
 *
 * ==============================================================================
 * The MIT License (MIT)
 *
 * Copyright (c) 2023 Albert Moky
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
package chat.dim.protocol;

import chat.dim.ext.CommandHandler;
import chat.dim.ext.SharedCommandExtensions;

/**
 *  Receipt command interface (message acknowledgment/receipt).
 *
 *  Used to send receipt/acknowledgment for a previously received message,
 *  confirming delivery or providing status feedback (via text).
 *
 *  Receipt Command
 *
 *  <blockquote><pre>
 *  data format: {
 *      "type" : i2s(0x88),
 *      "sn"   : 67890,
 *
 *      "command" : "receipt",
 *      "text"    : "...",  // text message
 *      "origin"  : {       // original message envelope
 *          "sender"    : "...",
 *          "receiver"  : "...",
 *          "time"      : 0,
 *
 *          "sn"        : 12345,
 *          "signature" : "..."
 *      }
 *  }
 *  </pre></blockquote>
 */
public interface ReceiptCommand extends Command {

    String RECEIPT   = "receipt";    // message receipt/acknowledgment

    /**
     *  Feedback text.
     */
    String getText();

    /**
     *  Envelope of the original message.
     */
    Envelope getOriginalEnvelope();

    /**
     *  Serial number of the original message for locating the chat history.
     */
    Long getOriginalSerialNumber();

    /**
     *  Signature of the original message for verification.
     */
    String getOriginalSignature();

    //
    //  Factories
    //

    /**
     *  Create base receipt command with text &amp; original message info
     */
    static ReceiptCommand create(String text, Envelope head, Content body) {
        CommandHandler helper = SharedCommandExtensions.handler;
        Command content = helper.createReceipt(text, head, body);
        if (content instanceof ReceiptCommand) {
            return  (ReceiptCommand) content;
        }
        assert false : "should not happen: " + content;
        return null;
    }

}
