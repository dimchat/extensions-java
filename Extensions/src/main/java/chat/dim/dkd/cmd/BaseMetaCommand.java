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

import java.util.Map;

import chat.dim.dkd.BaseCommand;
import chat.dim.protocol.ID;
import chat.dim.protocol.Meta;
import chat.dim.protocol.MetaCommand;


public class BaseMetaCommand extends BaseCommand implements MetaCommand {

    private Meta meta;

    /**
     * Create meta command with a raw map.
     *
     * @param content - raw command map.
     */
    public BaseMetaCommand(Map<String, Object> content) {
        super(content);
        // lazy
        meta = null;
    }

    /**
     * Create meta command with the given cmd, entity ID and meta.
     *
     * @param cmd  - defaults to {@link MetaCommand#META}
     * @param did  - entity ID
     * @param meta - entity meta (optional, null for querying)
     */
    public BaseMetaCommand(String cmd, ID did, Meta meta) {
        super(cmd);
        // ID
        assert did != null : "ID cannot be empty for meta command";
        put("did", did.toString());
        // meta
        if (meta != null) {
            put("meta", meta.toMap());
        }
        this.meta = meta;
    }

    /**
     *  Response Meta
     *
     * @param did  - entity ID
     * @param meta - entity Meta
     */
    public BaseMetaCommand(ID did, Meta meta) {
        this(META, did, meta);
    }

    /**
     *  Query Meta
     *
     * @param did - entity ID
     */
    public BaseMetaCommand(ID did) {
        this(META, did, null);
    }

    @Override
    public ID getIdentifier() {
        return ID.parse(get("did"));
    }

    @Override
    public Meta getMeta() {
        if (meta == null) {
            meta = Meta.parse(get("meta"));
        }
        return meta;
    }

}
