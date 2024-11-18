# RoadSense

<img alt="logo" height="512" src="app/src/main/res/drawable/splash_icon.png" width="512"/>

---
RoadSense è un'applicazione Android che rileva buche e dislivelli utilizzando i sensori dello smartphone

## Funzionalità
- **Rilevamento Buche**: Utilizza l'accelerometro del dispositivo per rilevare buche lungo la strada
- **Rilevamento Drops**: Monitora i cambiamenti nel giroscopio per rilevare i dislivelli
- **Mappa**: Mappa interattiva che mostra le posizioni geografiche delle rilevazioni
- **Reports**: Salvataggio delle rilevazioni in locale
- **Sincronizzazione**: Possibilità di sincronizzare le rilevazioni su un database centrale

## Tecnologie Utilizzate
- **Kotlin**: Linguaggio di programmazione principale
- **Jetpack Compose**: UI basata su composable functions per una navigazione moderna e reattiva
- **Hilt**: Dependency Injection per gestire iniezioni di dipendenze in modo semplice e scalabile
- **Room**: Database per la memorizzazione dei dati locali
- **MQTT on HiveMQ**: Protocollo di messaggistica e broker per inviare i dati di rilevamento al database centrale
- **OpenStreetMap**: Mappa del mondo fornita con licenza aperta

## Funzionamento
1. **Rilevamento delle buche**:
    - Avvia il rilevamento delle buche premendo il pulsante "Start Pothole Detection"
    - Una volta attivato, l'app rileva le buche e visualizza l'accelerazione del dispositivo lungo l'asse z su un grafico
    - Quando l'accelerazione del dispositivo supera una soglia impostata internamente viene rilevata una buca
    - Puoi interrompere il rilevamento con il pulsante "Stop Pothole Detection"

2. **Rilevamento dei Drops**:
    - Avvia il rilevamento dei dislivelli premendo il pulsante "Start Slope Detection"
    - Il giroscopio misura i cambiamenti nell'asse verticale del giroscopio per rilevare i dislivelli
    - La rilevazione inizia quando l'inclinazione supera una soglia superiore e termina quando viene raggiunta una seconda soglia inferiore
    - Interrompi il rilevamento con il pulsante "Stop Slope Detection"

## Requisiti
- **Android 14.0** o superiore (Si lo so, lo abbasserò prima o poi)
- **Permessi**: L'app richiede l'accesso ai sensori del dispositivo (accelerometro, giroscopio), alla posizione e ad internet

## Crediti
Creato da [Federico Chiodi](https://github.com/FedericoChiodi) per il progetto di industria 4.0 @UniFE

## Licenza
[Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0)