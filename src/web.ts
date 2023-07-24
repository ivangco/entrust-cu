import { WebPlugin } from '@capacitor/core';

import type { EntrustPlugin, PluginParams } from './definitions';

export class EntrustWeb extends WebPlugin implements EntrustPlugin {
  initializeSDK(): Promise<{response:Boolean}> {
    throw new Error('Method not implemented.');
  }
  
  getTokenOTP(): Promise<{ otp: string; }> {
    throw new Error('Method not implemented.');
  }
  activateTokenQr({ uri }: { uri: string; }): Promise<{ value: string; }> {
    throw new Error('Method not implemented.' + uri);
  }
  async activateTokenQuick(options: PluginParams): Promise<{ data: string }> {
    console.log('ECHO', options);
    return { data: "" };
  }

}
