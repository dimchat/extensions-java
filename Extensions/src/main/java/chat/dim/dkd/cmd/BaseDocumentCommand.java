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
package chat.dim.dkd.cmd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import chat.dim.protocol.Document;
import chat.dim.protocol.DocumentCommand;
import chat.dim.protocol.ID;
import chat.dim.protocol.Meta;


public class BaseDocumentCommand extends BaseMetaCommand implements DocumentCommand {

    private List<Document> documents;

    /**
     * Create document command with a raw map.
     *
     * @param content - raw command map.
     */
    public BaseDocumentCommand(Map<String, Object> content) {
        super(content);
        // lazy
        documents = null;
    }

    /**
     * Create document command for updating.
     *
     * @param did  - entity ID
     * @param meta - entity meta (optional)
     * @param docs - document list to update (optional)
     */
    public BaseDocumentCommand(ID did, Meta meta, List<Document> docs) {
        super(DOCUMENTS, did, meta);
        // documents
        if (docs != null) {
            put("documents", Document.revert(docs));
        }
        documents = docs;
    }

    /**
     * Create document command for querying.
     *
     * @param did  - entity ID
     * @param last - timestamp to query documents updated after it (optional)
     */
    public BaseDocumentCommand(ID did, Date last) {
        super(DOCUMENTS, did, null);
        // documents
        documents = null;
        // signature
        if (last != null) {
            setDateTime("last_time", last);
        }
    }

    @Override
    public List<Document> getDocuments() {
        List<Document> docs = documents;
        if (docs == null) {
            Object array = get("documents");
            if (array instanceof List) {
                docs = Document.convert((Iterable<?>) array);
            } else {
                assert array == null : "documents error: " + array;
                docs = new ArrayList<>();
            }
            documents = docs;
        }
        return docs;
    }

    @Override
    public Date getLastTime() {
        return getDateTime("last_time");
    }

}
