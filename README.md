<<<<<<< HEAD
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
* [`initializeSDK()`](#initializesdk)
* [`listTransactions(...)`](#listtransactions)
* [`completeChallenge(...)`](#completechallenge)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### activateTokenQuick(...)

```typescript
activateTokenQuick(param: ActivationParams) => Promise<{ data: string; }>
```

| Param       | Type                                                          |
| ----------- | ------------------------------------------------------------- |
| **`param`** | <code><a href="#activationparams">ActivationParams</a></code> |

**Returns:** <code>Promise&lt;{ data: string; }&gt;</code>

--------------------


### getTokenOTP(...)

```typescript
getTokenOTP(data: { jsonIdentity: string; }) => Promise<{ otp: string; }>
```

| Param      | Type                                   |
| ---------- | -------------------------------------- |
| **`data`** | <code>{ jsonIdentity: string; }</code> |

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


### initializeSDK()

```typescript
initializeSDK() => Promise<{ response: boolean; }>
```

**Returns:** <code>Promise&lt;{ response: boolean; }&gt;</code>

--------------------


### listTransactions(...)

```typescript
listTransactions(options: { jsonIdentity: string; }) => Promise<{ response: string; }>
```

| Param         | Type                                   |
| ------------- | -------------------------------------- |
| **`options`** | <code>{ jsonIdentity: string; }</code> |

**Returns:** <code>Promise&lt;{ response: string; }&gt;</code>

--------------------


### completeChallenge(...)

```typescript
completeChallenge(option: CompleteParams) => Promise<{ response: boolean; }>
```

| Param        | Type                                                      |
| ------------ | --------------------------------------------------------- |
| **`option`** | <code><a href="#completeparams">CompleteParams</a></code> |

**Returns:** <code>Promise&lt;{ response: boolean; }&gt;</code>

--------------------


### Interfaces


#### ActivationParams

| Prop               | Type                |
| ------------------ | ------------------- |
| **`serialNumber`** | <code>string</code> |
| **`regAddress`**   | <code>string</code> |
| **`regPassword`**  | <code>string</code> |


#### CompleteParams

| Prop                 | Type                                                        |
| -------------------- | ----------------------------------------------------------- |
| **`jsonIdentity`**   | <code>string</code>                                         |
| **`optionSelected`** | <code><a href="#completeoptions">CompleteOptions</a></code> |


### Type Aliases


#### CompleteOptions

<code>"CANCEL" | "CONFIRM"</code>

</docgen-api>

=======
# entrust-cu

Plugin para consumir SDK de Entrust
## Install

```bash
npm install entrust-cu
npx cap sync
```

## API

<docgen-index></docgen-index>

<docgen-api>
<!-- run docgen to generate docs from the source -->
<!-- More info: https://github.com/ionic-team/capacitor-docgen -->
</docgen-api>

>>>>>>> cambios-ios
