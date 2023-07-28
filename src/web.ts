import { WebPlugin } from '@capacitor/core';

import type { EntrustPlugin, ActivationParams } from './definitions';

export class EntrustWeb extends WebPlugin implements EntrustPlugin {
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

  async activateTokenQuick(options: ActivationParams): Promise<{ data: string }> {
    console.log('ECHO', options);
    return { data: "" };
  }

}
