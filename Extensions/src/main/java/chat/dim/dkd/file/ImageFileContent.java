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
package chat.dim.dkd.file;

import java.net.URI;
import java.util.Map;

import chat.dim.protocol.ContentType;
import chat.dim.protocol.DecryptKey;
import chat.dim.protocol.ImageContent;
import chat.dim.protocol.TransportableData;
import chat.dim.protocol.TransportableFile;


public class ImageFileContent extends BaseFileContent implements ImageContent {

    // small image
    private TransportableFile thumbnail = null;

    /**
     * Create image content with a raw map.
     *
     * @param content - raw map, usually from network or storage.
     */
    public ImageFileContent(Map<String, Object> content) {
        super(content);
    }

    /**
     * Create image content with the given components.
     *
     * @param data     - image data
     * @param filename - file name
     * @param url      - download URL
     * @param key      - decrypt key
     */
    public ImageFileContent(TransportableData data, String filename, URI url, DecryptKey key) {
        super(ContentType.IMAGE, data, filename, url, key);
    }

    @Override
    public Map<String, Object> toMap() {
        // serialize 'thumbnail'
        TransportableFile img = thumbnail;
        if (img != null && !containsKey("thumbnail")) {
            put("thumbnail", img.serialize());
        }
        // OK
        return super.toMap();
    }

    /*/
    @Override
    public TransportableFile toTransportableFile() {
        // serialize 'thumbnail'
        TransportableFile img = thumbnail;
        if (img != null && !containsKey("thumbnail")) {
            put("thumbnail", img.serialize());
        }
        // clone without other serializations
        return super.toTransportableFile();
    }
    /*/

    @Override
    public void setThumbnail(TransportableFile img) {
        remove("thumbnail");
        /*/
        if (img != null) {
            put("thumbnail", img.serialize());
        }
        /*/
        thumbnail = img;
    }

    @Override
    public TransportableFile getThumbnail() {
        TransportableFile img = thumbnail;
        if (img == null) {
            Object uri = get("thumbnail");
            img = TransportableFile.parse(uri);
            thumbnail = img;
        }
        return img;
    }

}
