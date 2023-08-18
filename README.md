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
* [`initializeSDK()`](#initializesdk)
* [`completeChallenge(...)`](#completechallenge)
* [`getDeviceFingerprint()`](#getdevicefingerprint)
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


### initializeSDK()

```typescript
initializeSDK() => Promise<{ response: boolean; }>
```

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
getDeviceFingerprint() => Promise<{ response: DeviceFingerprint; error: string; log: ObjectLog[]; }>
```

**Returns:** <code>Promise&lt;{ response: <a href="#devicefingerprint">DeviceFingerprint</a>; error: string; log: ObjectLog[]; }&gt;</code>

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


#### DeviceFingerprint

| Prop                        | Type                 |
| --------------------------- | -------------------- |
| **`simCountry`**            | <code>string</code>  |
| **`appVersion`**            | <code>string</code>  |
| **`screenWidth`**           | <code>number</code>  |
| **`timezone`**              | <code>string</code>  |
| **`locationAreaCode`**      | <code>number</code>  |
| **`timezoneOffsetString`**  | <code>string</code>  |
| **`locale`**                | <code>string</code>  |
| **`deviceId`**              | <code>string</code>  |
| **`connectionType`**        | <code>string</code>  |
| **`platform`**              | <code>string</code>  |
| **`manufacturer`**          | <code>string</code>  |
| **`deviceUnsecure`**        | <code>boolean</code> |
| **`carrierName`**           | <code>string</code>  |
| **`osVersion`**             | <code>string</code>  |
| **`timezoneOffset`**        | <code>number</code>  |
| **`model`**                 | <code>string</code>  |
| **`allowUnknownSources`**   | <code>boolean</code> |
| **`mobileCountryCode`**     | <code>string</code>  |
| **`mobileNetworkCode`**     | <code>string</code>  |
| **`screenHeight`**          | <code>number</code>  |
| **`cellId`**                | <code>string</code>  |
| **`primaryScramblingCode`** | <code>number</code>  |
| **`screenDensity`**         | <code>number</code>  |
| **`btName`**                | <code>string</code>  |


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
