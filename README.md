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
* [`initializeSDK(...)`](#initializesdk)
* [`completeChallenge(...)`](#completechallenge)
* [`getDeviceFingerprint()`](#getdevicefingerprint)
* [`getTransaction(...)`](#gettransaction)
* [`isDeveloperModeEnabled()`](#isdevelopermodeenabled)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### activateTokenQuick(...)

```typescript
activateTokenQuick(param: ActivationParams) => Promise<{ data: string; error: ActivationError; log: ObjectLog[]; }>
```

| Param       | Type                                                          |
| ----------- | ------------------------------------------------------------- |
| **`param`** | <code><a href="#activationparams">ActivationParams</a></code> |

**Returns:** <code>Promise&lt;{ data: string; error: <a href="#activationerror">ActivationError</a>; log: ObjectLog[]; }&gt;</code>

--------------------


### getTokenOTP(...)

```typescript
getTokenOTP(data: { jsonIdentity: string; }) => Promise<{ otp: string; error: string; log: ObjectLog[]; }>
```

| Param      | Type                                   |
| ---------- | -------------------------------------- |
| **`data`** | <code>{ jsonIdentity: string; }</code> |

**Returns:** <code>Promise&lt;{ otp: string; error: string; log: ObjectLog[]; }&gt;</code>

--------------------


### initializeSDK(...)

```typescript
initializeSDK({ appId, appVersion }: { appId: string; appVersion: string; }) => Promise<{ response: boolean; }>
```

| Param     | Type                                                |
| --------- | --------------------------------------------------- |
| **`__0`** | <code>{ appId: string; appVersion: string; }</code> |

**Returns:** <code>Promise&lt;{ response: boolean; }&gt;</code>

--------------------


### completeChallenge(...)

```typescript
completeChallenge(option: CompleteParams) => Promise<{ response: boolean; error: string; log: ObjectLog[]; }>
```

| Param        | Type                                                      |
| ------------ | --------------------------------------------------------- |
| **`option`** | <code><a href="#completeparams">CompleteParams</a></code> |

**Returns:** <code>Promise&lt;{ response: boolean; error: string; log: ObjectLog[]; }&gt;</code>

--------------------


### getDeviceFingerprint()

```typescript
getDeviceFingerprint() => Promise<{ response: string; error: string; log: ObjectLog[]; }>
```

**Returns:** <code>Promise&lt;{ response: string; error: string; log: ObjectLog[]; }&gt;</code>

--------------------


### getTransaction(...)

```typescript
getTransaction(data: { jsonIdentity: string; }) => Promise<{ response: boolean; error: string; log: ObjectLog[]; }>
```

| Param      | Type                                   |
| ---------- | -------------------------------------- |
| **`data`** | <code>{ jsonIdentity: string; }</code> |

**Returns:** <code>Promise&lt;{ response: boolean; error: string; log: ObjectLog[]; }&gt;</code>

--------------------


### isDeveloperModeEnabled()

```typescript
isDeveloperModeEnabled() => Promise<{ response: boolean; }>
```

**Returns:** <code>Promise&lt;{ response: boolean; }&gt;</code>

--------------------


### Interfaces


#### ObjectLog

| Prop                    | Type                |
| ----------------------- | ------------------- |
| **`estado`**            | <code>string</code> |
| **`fechaHora`**         | <code>string</code> |
| **`metodo`**            | <code>string</code> |
| **`parametrosEntrada`** | <code>string</code> |
| **`mensaje`**           | <code>string</code> |
| **`respuestaSalida`**   | <code>string</code> |


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


#### ActivationError

<code>"UNAUTHORIZED" | "REGPW_INVALID"</code>


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
