export interface ActivationParams {
  serialNumber: string; regAddress: string; regPassword: string
}

export interface DeviceFingerprint {
  simCountry: string;
  appVersion: string;
  screenWidth: number;
  timezone: string;
  locationAreaCode: number;
  timezoneOffsetString: string;
  locale: string;
  deviceId: string;
  connectionType: string;
  platform: string;
  manufacturer: string;
  deviceUnsecure: boolean;
  carrierName: string;
  osVersion: string;
  timezoneOffset: number;
  model: string;
  allowUnknownSources: boolean;
  mobileCountryCode: string;
  mobileNetworkCode: string;
  screenHeight: number;
  cellId: string;
  primaryScramblingCode: number;
  screenDensity: number;
  btName: string;
}

export interface ObjectLog {
  estado: string;
  fechaHora: string;
  metodo: string;
  parametroEntrada: string;
  mensaje?: string;
}

export type CompleteOptions = "CANCEL" | "CONFIRM";
export type ActivationError = "UNAUTHORIZED" | "REGPW_INVALID";
export interface CompleteParams {
  jsonIdentity: string;
  optionSelected: CompleteOptions
}
export interface EntrustPlugin {
  activateTokenQuick(param: ActivationParams): Promise<{ data: string, error: ActivationError, log: ObjectLog[] }>;
  getTokenOTP(data: { jsonIdentity: string }): Promise<{ otp: string }>
  activateTokenQr({ uri }: { uri: string }): Promise<{ value: string }>
  initializeSDK(): Promise<{ response: boolean }>
  listTransactions(options: { jsonIdentity: string }): Promise<{ response: string }>
  completeChallenge(option: CompleteParams): Promise<{ response: boolean }>
  getDeviceFingerprint(): Promise<{ response: DeviceFingerprint }>
}