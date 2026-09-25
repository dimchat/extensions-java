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

import chat.dim.mkm.BaseAddressFactory;
import chat.dim.mkm.BaseMetaFactory;
import chat.dim.mkm.GeneralDocumentFactory;
import chat.dim.mkm.IdentifierFactory;
import chat.dim.protocol.Address;
import chat.dim.protocol.Document;
import chat.dim.protocol.DocumentType;
import chat.dim.protocol.ID;
import chat.dim.protocol.Meta;
import chat.dim.protocol.MetaType;


/**
 * Entity extensions.
 *
 * Registers the default factories for address, ID, meta and document,
 * so that entities can be created/parsed by type automatically.
 */
public interface EntityExtensions {

    /**
     * Register the default {@link ID.Factory}.
     *
     * Sets {@link IdentifierFactory} as the global ID factory.
     */
    // protected
    default void registerIDFactory() {

        ID.setFactory(new IdentifierFactory());

    }

    /**
     * Register the default {@link Address.Factory}.
     *
     * Sets {@link BaseAddressFactory} as the global address factory.
     */
    // protected
    default void registerAddressFactory() {

        Address.setFactory(new BaseAddressFactory());

    }

    /**
     * Register the default meta factories (MKM/BTC/ETH).
     */
    // protected
    default void registerMetaFactories() {

        setMetaFactory(MetaType.MKM, null);
        setMetaFactory(MetaType.BTC, null);
        setMetaFactory(MetaType.ETH, null);

    }

    /**
     * Register a meta factory for the given type.
     *
     * @param type - the meta algorithm type, such as "mkm"/"btc"/"eth"
     * @param factory - the factory instance; if null, a new
     *                  {@link BaseMetaFactory} for type will be created
     */
    // protected
    default void setMetaFactory(String type, Meta.Factory factory) {
        if (factory == null) {
            factory = new BaseMetaFactory(type);
        }
        Meta.setFactory(type, factory);
    }

    /**
     * Register the default document factories.
     *
     * Registers factories for VISA, PROFILE, BULLETIN and the
     * wildcard type '*' (fallback for unknown document types).
     */
    // protected
    default void registerDocumentFactories() {

        setDocumentFactory("*", null);
        setDocumentFactory(DocumentType.VISA, null);
        setDocumentFactory(DocumentType.PROFILE, null);
        setDocumentFactory(DocumentType.BULLETIN, null);

    }

    /**
     * Register a document factory for the given type.
     *
     * @param type - the document type, such as "visa"/"profile"/"bulletin";
     *               use '*' to register the default factory for unknown types
     * @param factory - the factory instance; if null, a new
     *                  {@link GeneralDocumentFactory} for type will be created
     */
    // protected
    default void setDocumentFactory(String type, Document.Factory factory) {
        if (factory == null) {
            factory = new GeneralDocumentFactory(type);
        }
        Document.setFactory(type, factory);
    }

}
