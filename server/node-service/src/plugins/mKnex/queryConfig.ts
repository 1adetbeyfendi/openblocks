import { ConfigToType, QueryConfig } from "openblocks-sdk/dataSource";

const bucketActionParam = {
  key: "bucket",
  type: "textInput",
  label: "Bucket",
} as const;

const returnSignedUrlParam = {
  key: "returnSignedUrl",
  type: "switch",
  label: "Return signed url",
  placeholder: "false",
} as const;

enum SupabaseCategory {
  Storage = "Firestore",
}

const categories = {
  label: "Service",
  items: [{ label: "Storage", value: SupabaseCategory.Storage }],
};
// QueryConfig
const queryConfig = {
  type: "query",
  // categories,
  label: "Action",
  actions: [
    {
      actionName: "query",
      label: "Query",
      params: [
        {
          key: "sql",
          type: "sqlInput",
        },
      ],
    },
  ],
} as const;

export type ActionDataType = ConfigToType<typeof queryConfig>;

export default queryConfig;
