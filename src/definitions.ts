export interface EntrustPlugin {
  echo(options: { serialNumber: string, activationCode:string }): Promise<{ value: string }>;
}
