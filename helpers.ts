import { IntentError, IntentResponse } from "./interface";

export const getError = (
  message: string,
  error: any = undefined
): IntentError => {
  const obj: IntentError = {
    message,
    error
  };
  return obj;
};

export const getResponse = (response: any): IntentResponse => {
  const obj: IntentResponse = {
    value: response
  };
  return obj;
};
