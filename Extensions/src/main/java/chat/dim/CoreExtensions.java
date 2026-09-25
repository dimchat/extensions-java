/* license: https://mit-license.org
 *
 *  DIMP : Decentralized Instant Messaging Protocol
 *
 *                                Written in 2026 by Moky <albert.moky@gmail.com>
 *
 * ==============================================================================
 * The MIT License (MIT)
 *
 * Copyright (c) 2026 Albert Moky
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
package chat.dim;

import chat.dim.ext.GeneralAccountHelper;
import chat.dim.ext.GeneralCommandHelper;
import chat.dim.ext.GeneralMessageHelper;
import chat.dim.ext.SharedAccountExtensions;
import chat.dim.ext.SharedCommandExtensions;
import chat.dim.ext.SharedMessageExtensions;


/**
 * Core extensions.
 *
 * Registers the default account, message and command helpers
 * into the shared extension storages, so that the whole SDK
 * can create/parse entities and messages without extra setup.
 */
public interface CoreExtensions {

    /**
     * Register the account helpers (mkm).
     *
     * Sets {@link GeneralAccountHelper} as the default handler for
     * address/ID/meta/document parsing and generating.
     */
    // protected
    default void registerAccountHelpers() {

        // mkm
        GeneralAccountHelper accountHelper = new GeneralAccountHelper();
        SharedAccountExtensions.addressHelper = accountHelper;
        SharedAccountExtensions.idHelper      = accountHelper;
        SharedAccountExtensions.metaHelper    = accountHelper;
        SharedAccountExtensions.docHelper     = accountHelper;
        SharedAccountExtensions.handler       = accountHelper;

    }

    /**
     * Register the message helpers (dkd).
     *
     * Sets {@link GeneralMessageHelper} as the default handler for
     * content/envelope/instant/secure/reliable message operations.
     */
    // protected
    default void registerMessageHelpers() {

        // dkd
        GeneralMessageHelper msgHelper = new GeneralMessageHelper();
        SharedMessageExtensions.contentHelper  = msgHelper;
        SharedMessageExtensions.envelopeHelper = msgHelper;
        SharedMessageExtensions.instantHelper  = msgHelper;
        SharedMessageExtensions.secureHelper   = msgHelper;
        SharedMessageExtensions.reliableHelper = msgHelper;
        SharedMessageExtensions.handler        = msgHelper;

    }

    /**
     * Register the command helpers (cmd).
     *
     * Sets {@link GeneralCommandHelper} as the default handler for
     * command parsing and factory management.
     */
    // protected
    default void registerCommandHelpers() {

        // cmd
        GeneralCommandHelper cmdHelper = new GeneralCommandHelper();
        SharedCommandExtensions.commandHelper = cmdHelper;
        SharedCommandExtensions.handler       = cmdHelper;

    }

}
