import _ from "lodash";
import { DataSourcePlugin } from "openblocks-sdk/dataSource";
import { dataSourceConfig, DataSourceDataType } from "./dataSourceConfig";
import queryConfig, { ActionDataType } from "./queryConfig";
import run from "./run";
import { ServiceError } from "../../common/error";

const supabasePlugin: DataSourcePlugin<ActionDataType, DataSourceDataType> = {
  id: "mknex",
  name: "mknex",
  // icon: "supabase.svg",
  category: "api",
  dataSourceConfig: dataSourceConfig as any,
  // queryConfig,
  queryConfig,
  run: async function (actionData, dataSourceConfig, context) {
    try {
      // actionData.actionName

      return await run(actionData, dataSourceConfig);
    } catch (e: any) {
      if ((e as any).__isStorageError) {
        throw new ServiceError(e.message || e.cause);
      }
      throw e;
    }
  },
};

export default supabasePlugin;
