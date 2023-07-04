import { WebPlugin } from '@capacitor/core';

import type { EntrustPlugin } from './definitions';

export class EntrustWeb extends WebPlugin implements EntrustPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
