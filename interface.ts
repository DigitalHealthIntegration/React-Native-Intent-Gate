export interface IntentParams {
  action: string;
  title?: string;
  data?: any;
  extra?: IntentExtras;
  type?: string;
  requestCode?: number;
}

export interface IntentParamsComponent extends IntentParams {
  onResponse?: Function;
  invoke: boolean;
}

interface IntentExtras {
  [key: string]: string;
}

export interface IntentResponse {
  value: any;
}

export interface IntentError {
  message: string;
  error?: any;
}

export interface IntentNativeModule {
  openIntent: Function;
}

export interface IntentParamDefaults {
  TYPE: string;
  TITLE: string;
  REQUEST_CODE: string;
  EXTRA_KEY: string[];
  EXTRA_VALUE: string[];
}

export interface ResponseMessages {
  MODULE_NOT_FOUND: string;
  NO_ACTION: string;
  EXTRA_KEY_PAIR_ERROR: string;
  RESPONSE_FAIL: string;
  UNEXPECTED_ERROR: string;
}
