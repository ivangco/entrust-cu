export interface PluginParams {
  serialNumber: string; regAddress: string; regPassword: string
}

export interface EntrustPlugin {
  activateTokenQuick(options: PluginParams): Promise<{ data: string }>;
  getTokenOTP(options: { jsonIdentity: string }): Promise<{ otp: string }>
  activateTokenQr({ uri }: { uri: string }): Promise<{ value: string }>
}
