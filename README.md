**React Native App Intent Gate**

**Opens Android App Intent by Package Name and Actions**

**Features**

- Currently supports only on Android
- Send Extra key-values to intents and reads response from intents if any
- Promise based
- Types supported

**How to Use**

1. Install node module

   `npm i react-native-intent-gate`
   
   See https://www.npmjs.com/package/react-native-intent-gate for reference
2. Link the native module

   `react-native link react-native-intent-gate`

3. Import module in code

   `import {openIntent,OpenIntentComponent} from 'react-native-intent-gate'`

4. Code

   `const params = { action: "com.example.app", // only required extra: { key: value, title: "Open intent", requestCode: 12345, }`

   You can call openIntent in two ways:

   1. Open Intent using function
   
      `openIntent(params).then(console.log).catch(console.error);`

   2. Use as a component
   
      `<OpenIntentComponent invoke={true|false} onResponse={console.log} {...params} />`
      `/** when props updates from false to true, it will open intent */`

**Roadmap**

- UTs
- Support on iOS using URL Schema
- Intent data support on Android
