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
package chat.dim.protocol;

import chat.dim.dkd.cmd.BaseMetaCommand;

/**
 *  Meta command interface for querying/updating entity metadata.
 *
 *  Used to request or respond with an entity's core metadata (e.g. user/group info).
 *
 *  Meta Command
 *
 *  <blockquote><pre>
 *  data format: {
 *      "type" : i2s(0x88),
 *      "sn"   : 12345,
 *
 *      "command" : "meta", // command name
 *      "did"     : "{ID}", // contact's ID
 *      "meta"    : {...}   // when meta is null, means query meta for ID
 *  }
 *  </pre></blockquote>
 */
public interface MetaCommand extends Command {

    String META      = "meta";       // querying/updating entity metadata

    /**
     *  Contact identifier.
     */
    ID getIdentifier();

    /**
     *  Entity meta. Non-null: Response; Null: Query request.
     */
    Meta getMeta();

    //
    //  Factories
    //

    /**
     *  Creates a query meta command to request entity metadata.
     *
     *  Use this to ask for metadata of a specific entity (meta field will be null).
     *
     * @param did  target entity ID (user/group ID) to query
     * @return a MetaCommand instance for metadata query
     */
    static MetaCommand query(ID did) {
        return new BaseMetaCommand(did);
    }

    /**
     *  Creates a response meta command with entity metadata.
     *
     *  Use this to send metadata back to a query request.
     *
     * @param did  target entity ID (user/group ID)
     * @param meta metadata to return for the entity
     * @return a MetaCommand instance containing the metadata
     */
    static MetaCommand response(ID did, Meta meta) {
        return new BaseMetaCommand(did, meta);
    }

}
