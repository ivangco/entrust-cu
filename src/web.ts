import { WebPlugin } from '@capacitor/core';

import type { EntrustPlugin, ActivationParams, DeviceFingerprint } from './definitions';

export class EntrustWeb extends WebPlugin implements EntrustPlugin {

  getDeviceFingerprint(): Promise<{ response: DeviceFingerprint }> {
    throw new Error('Method not implemented.');
  }
  completeChallenge(): Promise<{ response: boolean; }> {
    throw new Error('Method not implemented.');
  }
  listTransactions(options: { jsonIdentity: string; }): Promise<{ response: string; }> {
    throw new Error('Method not implemented.' + options.jsonIdentity);
  }
  initializeSDK(): Promise<{ response: boolean }> {
    throw new Error('Method not implemented.');
  }

  getTokenOTP(): Promise<{ otp: string; }> {
    throw new Error('Method not implemented.');
  }

  activateTokenQr({ uri }: { uri: string; }): Promise<{ value: string; }> {
    throw new Error('Method not implemented.' + uri);
  }

  async activateTokenQuick(options: ActivationParams): Promise<{ data: string, error: string }> {
    console.log('ECHO', options);
    return { data: "", error: "" };
  }

}
