import { IntentParamDefaults, ResponseMessages } from "./interface";

export const INTENT_PARAM_DEFAULT: IntentParamDefaults = {
  TYPE: "text/json",
  TITLE: "Opening Intent",
  REQUEST_CODE: "0",
  EXTRA_KEY: [],
  EXTRA_VALUE: []
};

export const RESPONSE_MESSAGE: ResponseMessages = {
  MODULE_NOT_FOUND: "Native IntentModule not found.",
  NO_ACTION: "No action provided",
  EXTRA_KEY_PAIR_ERROR: "Failed to read extras key-values pair",
  RESPONSE_FAIL: "Failed to get response",
  UNEXPECTED_ERROR: "Unexpected error"
};
