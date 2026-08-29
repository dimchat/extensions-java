# DIM Application eXtensions (Java)

[![License](https://img.shields.io/github/license/dimchat/extensions-java)](https://github.com/dimchat/extensions-java/blob/master/LICENSE)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://github.com/dimchat/extensions-java/pulls)
[![Platform](https://img.shields.io/badge/Platform-Java%208-brightgreen.svg)](https://github.com/dimchat/extensions-java/wiki)
[![Issues](https://img.shields.io/github/issues/dimchat/extensions-java)](https://github.com/dimchat/extensions-java/issues)
[![Repo Size](https://img.shields.io/github/repo-size/dimchat/extensions-java)](https://github.com/dimchat/extensions-java/archive/refs/heads/master.zip)
[![Tags](https://img.shields.io/github/tag/dimchat/extensions-java)](https://github.com/dimchat/extensions-java/tags)
[![Version](https://img.shields.io/maven-central/v/chat.dim/Extensions)](https://mvnrepository.com/artifact/chat.dim/Extensions)

[![Watchers](https://img.shields.io/github/watchers/dimchat/extensions-java)](https://github.com/dimchat/extensions-java/watchers)
[![Forks](https://img.shields.io/github/forks/dimchat/extensions-java)](https://github.com/dimchat/extensions-java/forks)
[![Stars](https://img.shields.io/github/stars/dimchat/extensions-java)](https://github.com/dimchat/extensions-java/stargazers)
[![Followers](https://img.shields.io/github/followers/dimchat)](https://github.com/orgs/dimchat/followers)

## Dependencies

* Latest Versions

| Name | Version | Description |
|------|---------|-------------|
| [Cryptography](https://github.com/dimchat/mkm-java) | [![Version](https://img.shields.io/maven-central/v/chat.dim/Crypto)](https://mvnrepository.com/artifact/chat.dim/Crypto) | Crypto Keys |
| [Ming Ke Ming (名可名)](https://github.com/dimchat/mkm-java) | [![Version](https://img.shields.io/maven-central/v/chat.dim/MingKeMing)](https://mvnrepository.com/artifact/chat.dim/MingKeMing) | Decentralized User Identity Authentication |
| [Dao Ke Dao (道可道)](https://github.com/dimchat/dkd-java) | [![Version](https://img.shields.io/maven-central/v/chat.dim/DaoKeDao)](https://mvnrepository.com/artifact/chat.dim/DaoKeDao) | Universal Message Module |
| [DIMP (去中心化通讯协议)](https://github.com/dimchat/core-java) | [![Version](https://img.shields.io/maven-central/v/chat.dim/DIMP)](https://mvnrepository.com/artifact/chat.dim/DIMP) | Decentralized Instant Messaging Protocol |

## Extensions

1. Account
   * Address
	   * BTC
	   * ETH
   * Meta
	   * MKM _(Default)_
	   * BTC
	   * ETH
   * Document
	   * Visa _(User)_
	   * Profile
	   * Bulletin _(Group)_
2. Message Contents
   * Text Content
   * File Content
	   * Image Content
	   * Audio Content
	   * Video Content
   * Page Content
   * Name Card
   * Quote Content
   * Money Content
	   * Transfer Money
   * Combine Forward
3. System Commands
   * Meta Command
   * Document Command
   * Receipt Command
   * History Command
   * Group Command
	   * Invite
	   * Expel
	   * Query
	   * Quit
	   * Join

## Examples

### Address

```java
import chat.dim.mem.MemoryCache;
import chat.dim.mem.SharedAccountCache;
import chat.dim.mkm.BaseAddressFactory;
import chat.dim.protocol.Address;


public class CompatibleAddressFactory extends BaseAddressFactory {

    /**
     * Call it when received 'UIApplicationDidReceiveMemoryWarningNotification',
     * this will remove 50% of cached objects
     *
     * @return number of survivors
     */
    public int reduceMemory() {
        MemoryCache<String, Address> cache = SharedAccountCache.addressCache;
        return cache.reduceMemory();
    }

    @Override
    protected Address parse(String address) {
        try {
            Address res = super.parse(address);
            if (res != null) {
                return res;
            }
        } catch (Exception e) {
            // FIXME:
            e.printStackTrace();
            assert false : "invalid address: " + address;
        }
        //
        //  TODO: parse for other types of address
        //
        int len = address == null ? 0 : address.length();
        if (4 <= len && len <= 64) {
            return new UnknownAddress(address);
        }
        assert false : "invalid address: " + address;
        return null;
    }

}
```

```java
import chat.dim.protocol.Address;
import chat.dim.type.ConstantString;

public final class UnknownAddress extends ConstantString implements Address {

    public UnknownAddress(String string) {
        super(string);
    }

    @Override
    public int getNetwork() {
        return 0;  // EntityType.USER;
    }

}
```

### Meta

```java
import java.util.Map;

import chat.dim.mkm.*;
import chat.dim.ext.SharedAccountExtensions;
import chat.dim.protocol.Meta;

public final class CompatibleMetaFactory extends BaseMetaFactory {

    public CompatibleMetaFactory(String algorithm) {
        super(algorithm);
    }

    @Override
    public Meta parseMeta(Map<String, Object> meta) {
        Meta out;
        GeneralAccountHelper helper = SharedAccountExtensions.helper;
        String type = helper.getMetaType(info, "");
        switch (type) {

            case "MKM":
            case "mkm":
            case "1":
                out = new DefaultMeta(meta);
                break;

            case "BTC":
            case "btc":
            case "2":
                out = new BTCMeta(meta);
                break;

            case "ETH":
            case "eth":
            case "4":
                out = new ETHMeta(meta);
                break;

            default:
                throw new IllegalArgumentException("unknown meta type: " + type);
        }
        return out.isValid() ? out : null;
    }
}
```

### ExtensionLoader

```java
import chat.dim.protocol.*;
import chat.dim.dkd.*;
import chat.dim.*;

public class CommonExtensionLoader extends ExtensionLoader {

    @Override
    protected void registerAddressFactory() {
        
        Address.setFactory(new CompatibleAddressFactory());
        
    }

    @Override
    protected void registerMetaFactories() {

        Meta.Factory mkm = new CompatibleMetaFactory(MetaType.MKM);
        Meta.Factory btc = new CompatibleMetaFactory(MetaType.BTC);
        Meta.Factory eth = new CompatibleMetaFactory(MetaType.ETH);

        Meta.setFactory("1", mkm);
        Meta.setFactory("2", btc);
        Meta.setFactory("4", eth);

        Meta.setFactory("mkm", mkm);
        Meta.setFactory("btc", btc);
        Meta.setFactory("eth", eth);

        Meta.setFactory("MKM", mkm);
        Meta.setFactory("BTC", btc);
        Meta.setFactory("ETH", eth);
        
    }

    @Override
    public void registerContentFactories() {
        super.registerContentFactories();

        registerCustomizedFactories();
        
    }

    protected void registerCustomizedFactories() {

        // Application Customized
        Content.setFactory(ContentType.APPLICATION, AppCustomizedContent::new);
        Content.setFactory(ContentType.CUSTOMIZED, AppCustomizedContent::new);

    }

    @Override
    protected void registerCommandFactories() {
        super.registerCommandFactories();

        // Handshake
        setCommandFactory(HandshakeCommand.HANDSHAKE, HandshakeCommand::new);

    }

}
```

You must ensure that every ```Address``` you extend has a ```Meta``` type that can correspond to it one by one.

----

Copyright &copy; 2018-2026 Albert Moky
[![Followers](https://img.shields.io/github/followers/moky)](https://github.com/moky?tab=followers)
