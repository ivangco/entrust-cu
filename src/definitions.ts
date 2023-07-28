export interface ActivationParams {
  serialNumber: string; regAddress: string; regPassword: string
}

export type CompleteOptions = "CANCEL" | "CONFIRM";
export interface CompleteParams {
  jsonIdentity: string;
  optionSelected: CompleteOptions
}
export interface EntrustPlugin {
  activateTokenQuick(param: ActivationParams): Promise<{ data: string }>;
  getTokenOTP(data: { jsonIdentity: string }): Promise<{ otp: string }>
  activateTokenQr({ uri }: { uri: string }): Promise<{ value: string }>
  initializeSDK(): Promise<{ response: boolean }>
  listTransactions(options: { jsonIdentity: string }): Promise<{ response: string }>
  completeChallenge(option: CompleteParams): Promise<{ response: boolean }>
}