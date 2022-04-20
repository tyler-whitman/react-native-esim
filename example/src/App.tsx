import React, { useEffect, useState, useCallback } from 'react';
import { StyleSheet, View, Text, Button } from 'react-native';
import EsimManager, { EsimSetupResultStatus } from 'react-native-esim';

export default function App() {
  const [supported, setSupported] = useState<boolean>();

  const supportStatus = supported ? 'Supported' : 'Not supported';

  useEffect((): void => {
    EsimManager.isEsimSupported().then(setSupported);
  }, []);

  const openCellularSettings = useCallback(async (): Promise<void> => {
    try {
      await EsimManager.openCellularSettings();
    } catch (error) {
      console.error(error);
    }
  }, []);

  const performSetup = useCallback(async (): Promise<void> => {
    try {
      const result = await EsimManager.setupEsim({ address: 'address.com' });
      console.log(result);
      switch (result) {
        case EsimSetupResultStatus.Unknown:
          console.log('esim setup unknown');
          break;
        case EsimSetupResultStatus.Fail:
          console.log('esim setup fail');
          break;
        case EsimSetupResultStatus.Success:
          console.log('esim setup success');
          break;
      }
    } catch (error) {
      console.error(error);
    }
  }, []);

  return (
    <View style={styles.container}>
      <Text>
        Support status: {supported === undefined ? 'Loading...' : supportStatus}
      </Text>
      <View style={styles.separator} />
      <Button title="Setup" onPress={performSetup} />
      <View style={styles.separator} />
      <Button title="Open Cellular Settings" onPress={openCellularSettings} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  separator: {
    marginTop: 8,
  },
});
