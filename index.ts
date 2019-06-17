import { NativeModules } from "react-native";
import React from "react";
import { getResponse, getError } from "./helpers";
import {
  IntentParams,
  IntentResponse,
  IntentParamsComponent,
  IntentNativeModule
} from "./interface";
import { INTENT_PARAM_DEFAULT, RESPONSE_MESSAGE } from "./constants";
const IntentModule: IntentNativeModule = NativeModules.RNIntentModule;

export const openIntent = (
  intentParams: IntentParams
): Promise<IntentResponse> => {
  return new Promise((resolve, reject) => {
    try {
      if (!IntentModule) {
        reject(getError(RESPONSE_MESSAGE.MODULE_NOT_FOUND));
      }
      if (!intentParams.action) {
        reject(getError(RESPONSE_MESSAGE.NO_ACTION));
      }

      const extraKeys = intentParams.extra
        ? Object.keys(intentParams.extra)
        : [];
      const extraValues: string[] = [];

      extraKeys.forEach(thisKey => {
        if (intentParams.extra) {
          extraValues.push(intentParams.extra[thisKey]);
        } else {
          reject(getError(RESPONSE_MESSAGE.EXTRA_KEY_PAIR_ERROR));
        }
      });

      const extraKeysToSend = extraKeys.length
        ? extraKeys
        : INTENT_PARAM_DEFAULT.EXTRA_KEY;
      const extraValuesToSend = extraValues.length
        ? extraValues
        : INTENT_PARAM_DEFAULT.EXTRA_VALUE;
      const requestCode = intentParams.requestCode
        ? intentParams.requestCode.toString()
        : INTENT_PARAM_DEFAULT.REQUEST_CODE;
      const type = intentParams.type || INTENT_PARAM_DEFAULT.TYPE;
      const title = intentParams.title || INTENT_PARAM_DEFAULT.TITLE;

      IntentModule.openIntent(
        intentParams.action,
        title,
        extraKeysToSend,
        extraValuesToSend,
        requestCode, // as string, else type casting fails in bridge bcoz of bug in react-native: converts number to double
        type
      )
        .then((response: Object) => {
          resolve(getResponse(response));
        })
        .catch((error: any) => {
          reject(getError(RESPONSE_MESSAGE.RESPONSE_FAIL, error));
        });
    } catch (err) {
      reject(getError(RESPONSE_MESSAGE.UNEXPECTED_ERROR, err));
    }
  });
};

export class OpenIntentComponent extends React.Component<
  IntentParamsComponent
> {
  responseHandler = (intentData: Object) => {
    if (!this.props.onResponse) {
      return;
    }
    this.props.onResponse(intentData);
  };

  invokeHandler = () => {
    openIntent(this.props)
      .then(this.responseHandler)
      .catch(this.responseHandler);
  };

  componentWillUpdate(nextProps: IntentParamsComponent) {
    if (!this.props.invoke && nextProps.invoke === true) {
      this.invokeHandler();
    }
  }

  render() {
    return null; // No UI
  }
}

export default openIntent;
