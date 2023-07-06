export interface EntrustPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
