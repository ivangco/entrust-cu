import { WebPlugin } from '@capacitor/core';

import type { EntrustPlugin, ActivationParams, DeviceFingerprint, ActivationError, ObjectLog } from './definitions';

export class EntrustWeb extends WebPlugin implements EntrustPlugin {
  activateTokenQuick(param: ActivationParams): Promise<{ data: string; error: ActivationError; log: ObjectLog[]; }> {
    throw new Error('Method not implemented.' + param);
  }

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

}
