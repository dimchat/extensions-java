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

import chat.dim.dkd.BaseContent;
import chat.dim.ext.SharedFileFormatExtensions;
import chat.dim.format.TransportableFileWrapper;
import chat.dim.protocol.DecryptKey;
import chat.dim.protocol.FileContent;
import chat.dim.protocol.TransportableData;


public class BaseFileContent extends BaseContent implements FileContent {

    // private
    public final TransportableFileWrapper wrapper;

    public BaseFileContent(Map<String, Object> content) {
        super(content);
        TransportableFileWrapper.Factory factory = SharedFileFormatExtensions.pnfWrapperFactory;
        wrapper = factory.createTransportableFileWrapper(super.toMap());
    }

    public BaseFileContent(String type, TransportableData data, String filename, URI url, DecryptKey password) {
        super(type);
        TransportableFileWrapper.Factory factory = SharedFileFormatExtensions.pnfWrapperFactory;
        wrapper = factory.createTransportableFileWrapper(super.toMap(), data, filename, url, password);
    }

    @Override
    public Map<String, Object> toMap() {
        // call wrapper to serialize 'data' & 'key"
        return wrapper.toMap();
    }

    /*/
    @Override
    public TransportableFile toTransportableFile() {
        // clone without serializations
        return new PortableNetworkFile(super.toMap(), wrapper);
    }
    /*/

    /**
     *  file data
     */

    @Override
    public TransportableData getData() {
        return wrapper.getData();
    }

    @Override
    public void setData(TransportableData data) {
        wrapper.setData(data);
    }

    /**
     *  file name
     */

    @Override
    public String getFilename() {
        return wrapper.getFilename();
    }

    @Override
    public void setFilename(String name) {
        wrapper.setFilename(name);
    }

    /**
     *  download URL
     */

    @Override
    public URI getURL() {
        return wrapper.getURL();
    }

    @Override
    public void setURL(URI url) {
        wrapper.setURL(url);
    }

    /**
     *  decrypt key
     */

    @Override
    public DecryptKey getPassword() {
        return wrapper.getPassword();
    }

    @Override
    public void setPassword(DecryptKey key) {
        wrapper.setPassword(key);
    }

}
