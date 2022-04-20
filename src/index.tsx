import { Linking, NativeModules, Platform } from 'react-native';

const { RNESimManager } = NativeModules;

export type EsimConfig = {
  address: string;
  confirmationCode?: string;
  eid?: string;
  iccid?: string;
  matchingId?: string;
  oid?: string;
};

export enum EsimSetupResultStatus {
  Unknown = 0,
  Fail = 1,
  Success = 2,
}

const openCellularSettings = async () => {
  if (Platform.OS === 'android') {
    await Linking.sendIntent(
      'android.settings.MANAGE_ALL_SIM_PROFILES_SETTINGS'
    );
  } else {
    await Linking.openURL('App-Prefs:MOBILE_DATA_SETTINGS_ID');
  }
};

type EsimManager = {
  setupEsim(config: EsimConfig): Promise<EsimSetupResultStatus | never>;
  isEsimSupported(): Promise<boolean | never>;
  openCellularSettings(): Promise<void>;
};

const Manager: EsimManager = {
  openCellularSettings,
  ...RNESimManager,
};

export default Manager;
