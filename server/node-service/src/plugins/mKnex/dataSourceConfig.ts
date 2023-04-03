import { Config, ConfigToType } from "openblocks-sdk/dataSource";

export const dataSourceConfig = {
  type: "dataSource",
  params: [
    {
      key: "db_type",
      type: "select",
      options: [
        {
          label: "mysql",
          value: "mysql",
        },
      ],
    },
    {
      type: "textInput",
      key: "host",
      label: "Host",
      rules: [{ required: true }],
    },
    {
      type: "textInput",
      key: "port",
      label: "Port",
      rules: [{ required: true }],
    },

    {
      key: "database",
      type: "textInput",
      label: "Database",
    },

    {
      type: "textInput",
      key: "userName",
      label: "userName",
      rules: [{ required: true }],
    },
    {
      type: "password",
      key: "password",
      label: "Password",
    },
  ],
} as const;

export type DataSourceDataType = ConfigToType<typeof dataSourceConfig>;
