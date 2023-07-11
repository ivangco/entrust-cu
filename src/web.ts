import { WebPlugin } from '@capacitor/core';

import type { EntrustPlugin } from './definitions';

export class EntrustWeb extends WebPlugin implements EntrustPlugin {
  async echo(options: { serialNumber: string, activationCode: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return { value: "echo" };
  }
}
