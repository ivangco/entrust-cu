# entrust-cu

Plugin para consumir SDK de Entrust
## Install

```bash
npm install entrust-cu
npx cap sync
```

## API

<docgen-index>

* [`activateTokenQuick(...)`](#activatetokenquick)
* [`getTokenOTP(...)`](#gettokenotp)
* [`activateTokenQr(...)`](#activatetokenqr)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### activateTokenQuick(...)

```typescript
activateTokenQuick(options: PluginParams) => Promise<{ data: string; }>
```

| Param         | Type                                                  |
| ------------- | ----------------------------------------------------- |
| **`options`** | <code><a href="#pluginparams">PluginParams</a></code> |

**Returns:** <code>Promise&lt;{ data: string; }&gt;</code>

--------------------


### getTokenOTP(...)

```typescript
getTokenOTP(options: { jsonIdentity: string; }) => Promise<{ otp: string; }>
```

| Param         | Type                                   |
| ------------- | -------------------------------------- |
| **`options`** | <code>{ jsonIdentity: string; }</code> |

**Returns:** <code>Promise&lt;{ otp: string; }&gt;</code>

--------------------


### activateTokenQr(...)

```typescript
activateTokenQr({ uri }: { uri: string; }) => Promise<{ value: string; }>
```

| Param     | Type                          |
| --------- | ----------------------------- |
| **`__0`** | <code>{ uri: string; }</code> |

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### Interfaces


#### PluginParams

| Prop               | Type                |
| ------------------ | ------------------- |
| **`serialNumber`** | <code>string</code> |
| **`regAddress`**   | <code>string</code> |
| **`regPassword`**  | <code>string</code> |

</docgen-api>

