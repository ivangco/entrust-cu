import { registerPlugin, } from '@capacitor/core';

import type { EntrustPlugin } from './definitions';

const Entrust = registerPlugin<EntrustPlugin>('Entrust',{
  web: () => import('./web').then(m => new m.EntrustWeb()),
});

export * from './definitions';
export { Entrust };
