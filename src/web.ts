import { WebPlugin } from '@capacitor/core';

import type { EntrustPlugin, ActivationParams, DeviceFingerprint, ActivationError, ObjectLog, CompleteParams } from './definitions';

export class EntrustWeb extends WebPlugin implements EntrustPlugin {
  getTransaction(data: { jsonIdentity: string; }): Promise<{ response: boolean; error: string; log: ObjectLog[]; }> {
    throw new Error('Method not implemented.'+data);
  }
  getDeviceFingerprint(): Promise<{ response: DeviceFingerprint; error: string; log: ObjectLog[]; }> {
    throw new Error('Method not implemented.');
  }
  completeChallenge(option: CompleteParams): Promise<{ response: boolean; error: string; log: ObjectLog[]; }> {
    throw new Error('Method not implemented.' + option);
  }
  getTokenOTP(data: { jsonIdentity: string; }): Promise<{ otp: string; error: string; log: ObjectLog[]; }> {
    throw new Error('Method not implemented.' + data);
  }
  activateTokenQuick(param: ActivationParams): Promise<{ data: string; error: ActivationError; log: ObjectLog[]; }> {
    throw new Error('Method not implemented.' + param);
  }
  initializeSDK(): Promise<{ response: boolean }> {
    throw new Error('Method not implemented.');
  }
}
