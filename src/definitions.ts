export interface ActivationParams {
  serialNumber: string; regAddress: string; regPassword: string
}

export interface DeviceFingerprint {
  platform: string;
  version: string;
  attributes: Attributes;
}

interface Attributes {
  simCountry: string;
  allowUnknownSources: boolean;
  appVersion: string;
  screenWidth: number;
  mobileCountryCode: string;
  mobileNetworkCode: string;
  timezone: string;
  screenHeight: number;
  locale: string;
  deviceId: string;
  platform: string;
  manufacturer: string;
  deviceUnsecure: boolean;
  carrierName: string;
  osVersion: string;
  timezoneOffset: number;
  model: string;
  screenDensity: number;
  modelName:string;
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
  initializeSDK({ appId, appVersion }: { appId: string, appVersion: string }): Promise<{ response: boolean }>
  completeChallenge(option: CompleteParams): Promise<{ response: boolean, error: string, log: ObjectLog[] }>
  getDeviceFingerprint(): Promise<{ response: string, error: string, log: ObjectLog[] }>
  getTransaction(data: { jsonIdentity: string }): Promise<{ response: boolean, error: string, log: ObjectLog[] }>
  isDeveloperModeEnabled(): Promise<{ response: boolean }>
}