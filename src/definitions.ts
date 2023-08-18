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
  parametrosEntrada: string;
  mensaje?: string;
  respuestaSalida?: string;
}

export type CompleteOptions = "CANCEL" | "CONFIRM";
export type ActivationError = "UNAUTHORIZED" | "REGPW_INVALID";
export interface CompleteParams {
  jsonIdentity: string;
  optionSelected: CompleteOptions
}

export interface EntrustPlugin {
  activateTokenQuick(param: ActivationParams): Promise<{ data: string, error: ActivationError, log: ObjectLog[] }>;
  getTokenOTP(data: { jsonIdentity: string }): Promise<{ otp: string, error: string, log: ObjectLog[] }>
  initializeSDK(): Promise<{ response: boolean }>
  completeChallenge(option: CompleteParams): Promise<{ response: boolean, error: string, log: ObjectLog[] }>
  getDeviceFingerprint(): Promise<{ response: DeviceFingerprint, error: string, log: ObjectLog[] }>
}