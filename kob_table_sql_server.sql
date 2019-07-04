SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[kob_job_cron]') AND type in (N'U'))
BEGIN
CREATE TABLE [kob_job_cron](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[project_code] [varchar](60) NOT NULL,
	[project_name] [varchar](60) NOT NULL,
	[job_uuid] [varchar](60) NOT NULL,
	[job_type] [varchar](64) NOT NULL,
	[job_cn] [varchar](60) NOT NULL,
	[task_key] [varchar](60) NOT NULL,
	[task_type] [varchar](60) NOT NULL,
	[task_remark] [varchar](60) NOT NULL,
	[load_balance] [varchar](60) NOT NULL,
	[suspend] [int] NULL,
	[batch_type] [varchar](60) NOT NULL,
	[retry_type] [varchar](60) NOT NULL,
	[failover] [int] NULL,
	[rely] [int] NULL,
	[cron_expression] [varchar](128) NOT NULL,
	[user_params] [varchar](max) NULL,
	[inner_params] [varchar](max) NULL,
	[last_generate_trigger_time] [bigint] NULL,
	[timeout_threshold] [bigint] NULL,
	[retry_count] [int] NULL,
	[version] [int] NOT NULL,
	[gmt_created] [datetime] NOT NULL,
	[gmt_modified] [datetime] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
END
GO
/****** Object:  Table [kob_log_collect]    Script Date: 2019/5/10 18:39:58 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[kob_log_collect]') AND type in (N'U'))
BEGIN
CREATE TABLE [kob_log_collect](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[state] [int] NULL,
	[log_uuid] [varchar](60) NOT NULL,
	[project_code] [varchar](60) NOT NULL,
	[task_uuid] [varchar](60) NOT NULL,
	[log_mode] [varchar](60) NOT NULL,
	[log_level] [varchar](60) NOT NULL,
	[client_identification] [varchar](60) NOT NULL,
	[msg] [varchar](max) NULL,
	[log_time] [datetime] NULL,
	[gmt_created] [datetime] NOT NULL,
	[gmt_modified] [datetime] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
END
GO
/****** Object:  Table [kob_log_opt]    Script Date: 2019/5/10 18:39:58 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[kob_log_opt]') AND type in (N'U'))
BEGIN
CREATE TABLE [kob_log_opt](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[user_code] [varchar](100) NOT NULL,
	[user_name] [varchar](100) NOT NULL,
	[opt_url] [varchar](200) NOT NULL,
	[request] [varchar](max) NULL,
	[response] [varchar](max) NULL,
	[cost_time] [int] NULL,
	[gmt_created] [datetime] NOT NULL,
	[gmt_modified] [datetime] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
END
GO
/****** Object:  Table [kob_project_user]    Script Date: 2019/5/10 18:39:58 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[kob_project_user]') AND type in (N'U'))
BEGIN
CREATE TABLE [kob_project_user](
	[id] [int] IDENTITY(2,1) NOT NULL,
	[user_code] [nvarchar](50) NOT NULL,
	[user_name] [nvarchar](100) NOT NULL,
	[project_code] [nvarchar](100) NOT NULL,
	[project_name] [nvarchar](100) NOT NULL,
	[project_mode] [nvarchar](100) NOT NULL,
	[owner] [smallint] NULL,
	[configuration] [nvarchar](max) NULL,
	[version] [int] NOT NULL,
	[gmt_created] [datetime] NOT NULL,
	[gmt_modified] [datetime] NOT NULL,
 CONSTRAINT [PK_kob_project_user_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [kob_project_user$uniq_user_code_project_code] UNIQUE NONCLUSTERED 
(
	[user_code] ASC,
	[project_code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
END
GO
/****** Object:  Table [kob_task_record]    Script Date: 2019/5/10 18:39:58 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[kob_task_record]') AND type in (N'U'))
BEGIN
CREATE TABLE [kob_task_record](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[project_code] [varchar](60) NOT NULL,
	[project_name] [varchar](60) NOT NULL,
	[job_uuid] [varchar](60) NOT NULL,
	[job_type] [varchar](60) NOT NULL,
	[job_cn] [varchar](60) NOT NULL,
	[task_key] [varchar](60) NOT NULL,
	[task_remark] [varchar](60) NOT NULL,
	[task_type] [varchar](60) NOT NULL,
	[task_uuid] [varchar](60) NOT NULL,
	[relation_task_uuid] [varchar](60) NOT NULL,
	[retry_type] [varchar](60) NOT NULL,
	[rely] [int] NULL,
	[ancestor] [int] NULL,
	[user_params] [varchar](max) NULL,
	[inner_params] [varchar](max) NULL,
	[msg] [varchar](max) NULL,
	[cron_expression] [varchar](128) NOT NULL,
	[timeout_threshold] [bigint] NULL,
	[failover] [int] NULL,
	[load_balance] [varchar](60) NOT NULL,
	[state] [int] NULL,
	[complete] [int] NULL,
	[retry_count] [int] NULL,
	[batch_type] [varchar](60) NOT NULL,
	[client_identification] [varchar](40) NOT NULL,
	[trigger_time] [bigint] NULL,
	[consumption_time] [datetime] NULL,
	[execute_start_time] [datetime] NULL,
	[execute_end_time] [datetime] NULL,
	[version] [bigint] NOT NULL,
	[gmt_created] [datetime] NOT NULL,
	[gmt_modified] [datetime] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
END
GO
/****** Object:  Table [kob_task_waiting]    Script Date: 2019/5/10 18:39:58 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[kob_task_waiting]') AND type in (N'U'))
BEGIN
CREATE TABLE [kob_task_waiting](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[project_code] [varchar](60) NOT NULL,
	[project_name] [varchar](60) NOT NULL,
	[job_uuid] [varchar](60) NOT NULL,
	[job_type] [varchar](60) NOT NULL,
	[job_cn] [varchar](60) NOT NULL,
	[task_key] [varchar](60) NOT NULL,
	[task_remark] [varchar](60) NOT NULL,
	[task_type] [varchar](60) NOT NULL,
	[task_uuid] [varchar](60) NOT NULL,
	[relation_task_uuid] [varchar](60) NOT NULL,
	[retry_type] [varchar](60) NOT NULL,
	[batch_type] [varchar](60) NOT NULL,
	[failover] [int] NULL,
	[load_balance] [varchar](60) NOT NULL,
	[rely] [int] NULL,
	[user_params] [varchar](max) NULL,
	[inner_params] [varchar](max) NULL,
	[cron_expression] [varchar](128) NOT NULL,
	[timeout_threshold] [bigint] NULL,
	[retry_count] [int] NULL,
	[trigger_time] [bigint] NULL,
	[version] [int] NOT NULL,
	[gmt_created] [datetime] NOT NULL,
	[gmt_modified] [datetime] NOT NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
END
GO
/****** Object:  Table [kob_user]    Script Date: 2019/5/10 18:39:58 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[kob_user]') AND type in (N'U'))
BEGIN
CREATE TABLE [kob_user](
	[id] [int] IDENTITY(3,1) NOT NULL,
	[code] [nvarchar](100) NOT NULL,
	[name] [nvarchar](100) NOT NULL,
	[pwd] [nvarchar](100) NOT NULL,
	[configuration] [nvarchar](max) NULL,
	[version] [int] NOT NULL,
	[gmt_created] [datetime] NOT NULL,
	[gmt_modified] [datetime] NOT NULL,
 CONSTRAINT [PK_kob_user_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_job_c__proje__4CA06362]') AND type = 'D')
BEGIN
ALTER TABLE [kob_job_cron] ADD  DEFAULT ('') FOR [project_code]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_job_c__proje__4D94879B]') AND type = 'D')
BEGIN
ALTER TABLE [kob_job_cron] ADD  DEFAULT ('') FOR [project_name]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_job_c__job_u__4E88ABD4]') AND type = 'D')
BEGIN
ALTER TABLE [kob_job_cron] ADD  DEFAULT ('') FOR [job_uuid]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_job_c__job_t__4F7CD00D]') AND type = 'D')
BEGIN
ALTER TABLE [kob_job_cron] ADD  DEFAULT ('') FOR [job_type]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_job_c__job_c__5070F446]') AND type = 'D')
BEGIN
ALTER TABLE [kob_job_cron] ADD  DEFAULT ('') FOR [job_cn]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_job_c__task___5165187F]') AND type = 'D')
BEGIN
ALTER TABLE [kob_job_cron] ADD  DEFAULT ('') FOR [task_key]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_job_c__task___52593CB8]') AND type = 'D')
BEGIN
ALTER TABLE [kob_job_cron] ADD  DEFAULT ('') FOR [task_type]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_job_c__task___534D60F1]') AND type = 'D')
BEGIN
ALTER TABLE [kob_job_cron] ADD  DEFAULT ('') FOR [task_remark]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_job_c__load___5441852A]') AND type = 'D')
BEGIN
ALTER TABLE [kob_job_cron] ADD  DEFAULT ('') FOR [load_balance]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_job_c__suspe__5535A963]') AND type = 'D')
BEGIN
ALTER TABLE [kob_job_cron] ADD  DEFAULT (NULL) FOR [suspend]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_job_c__batch__5629CD9C]') AND type = 'D')
BEGIN
ALTER TABLE [kob_job_cron] ADD  DEFAULT ('') FOR [batch_type]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_job_c__retry__571DF1D5]') AND type = 'D')
BEGIN
ALTER TABLE [kob_job_cron] ADD  DEFAULT ('') FOR [retry_type]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_job_c__failo__5812160E]') AND type = 'D')
BEGIN
ALTER TABLE [kob_job_cron] ADD  DEFAULT (NULL) FOR [failover]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_job_cr__rely__59063A47]') AND type = 'D')
BEGIN
ALTER TABLE [kob_job_cron] ADD  DEFAULT (NULL) FOR [rely]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_job_c__cron___59FA5E80]') AND type = 'D')
BEGIN
ALTER TABLE [kob_job_cron] ADD  DEFAULT ('') FOR [cron_expression]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_job_c__last___5AEE82B9]') AND type = 'D')
BEGIN
ALTER TABLE [kob_job_cron] ADD  DEFAULT (NULL) FOR [last_generate_trigger_time]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_job_c__timeo__5BE2A6F2]') AND type = 'D')
BEGIN
ALTER TABLE [kob_job_cron] ADD  DEFAULT (NULL) FOR [timeout_threshold]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_job_c__retry__5CD6CB2B]') AND type = 'D')
BEGIN
ALTER TABLE [kob_job_cron] ADD  DEFAULT (NULL) FOR [retry_count]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_job_c__versi__5DCAEF64]') AND type = 'D')
BEGIN
ALTER TABLE [kob_job_cron] ADD  DEFAULT ('0') FOR [version]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_job_c__gmt_c__5EBF139D]') AND type = 'D')
BEGIN
ALTER TABLE [kob_job_cron] ADD  DEFAULT (getdate()) FOR [gmt_created]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_job_c__gmt_m__5FB337D6]') AND type = 'D')
BEGIN
ALTER TABLE [kob_job_cron] ADD  DEFAULT (getdate()) FOR [gmt_modified]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_log_c__state__1DB06A4F]') AND type = 'D')
BEGIN
ALTER TABLE [kob_log_collect] ADD  DEFAULT (NULL) FOR [state]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_log_c__log_u__1EA48E88]') AND type = 'D')
BEGIN
ALTER TABLE [kob_log_collect] ADD  DEFAULT ('') FOR [log_uuid]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_log_c__proje__1F98B2C1]') AND type = 'D')
BEGIN
ALTER TABLE [kob_log_collect] ADD  DEFAULT ('') FOR [project_code]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_log_c__task___208CD6FA]') AND type = 'D')
BEGIN
ALTER TABLE [kob_log_collect] ADD  DEFAULT ('') FOR [task_uuid]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_log_c__log_m__2180FB33]') AND type = 'D')
BEGIN
ALTER TABLE [kob_log_collect] ADD  DEFAULT ('') FOR [log_mode]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_log_c__log_l__22751F6C]') AND type = 'D')
BEGIN
ALTER TABLE [kob_log_collect] ADD  DEFAULT ('') FOR [log_level]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_log_c__clien__236943A5]') AND type = 'D')
BEGIN
ALTER TABLE [kob_log_collect] ADD  DEFAULT ('') FOR [client_identification]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_log_c__log_t__245D67DE]') AND type = 'D')
BEGIN
ALTER TABLE [kob_log_collect] ADD  DEFAULT (NULL) FOR [log_time]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_log_c__gmt_c__25518C17]') AND type = 'D')
BEGIN
ALTER TABLE [kob_log_collect] ADD  DEFAULT (getdate()) FOR [gmt_created]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_log_c__gmt_m__2645B050]') AND type = 'D')
BEGIN
ALTER TABLE [kob_log_collect] ADD  DEFAULT (getdate()) FOR [gmt_modified]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_log_o__user___29221CFB]') AND type = 'D')
BEGIN
ALTER TABLE [kob_log_opt] ADD  DEFAULT ('') FOR [user_code]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_log_o__user___2A164134]') AND type = 'D')
BEGIN
ALTER TABLE [kob_log_opt] ADD  DEFAULT ('') FOR [user_name]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_log_o__opt_u__2B0A656D]') AND type = 'D')
BEGIN
ALTER TABLE [kob_log_opt] ADD  DEFAULT ('') FOR [opt_url]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_log_o__cost___2BFE89A6]') AND type = 'D')
BEGIN
ALTER TABLE [kob_log_opt] ADD  DEFAULT (NULL) FOR [cost_time]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_log_o__gmt_c__2CF2ADDF]') AND type = 'D')
BEGIN
ALTER TABLE [kob_log_opt] ADD  DEFAULT (getdate()) FOR [gmt_created]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_log_o__gmt_m__2DE6D218]') AND type = 'D')
BEGIN
ALTER TABLE [kob_log_opt] ADD  DEFAULT (getdate()) FOR [gmt_modified]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_proje__user___31B762FC]') AND type = 'D')
BEGIN
ALTER TABLE [kob_project_user] ADD  DEFAULT (N'') FOR [user_code]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_proje__user___32AB8735]') AND type = 'D')
BEGIN
ALTER TABLE [kob_project_user] ADD  DEFAULT (N'') FOR [user_name]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_proje__proje__339FAB6E]') AND type = 'D')
BEGIN
ALTER TABLE [kob_project_user] ADD  DEFAULT (N'') FOR [project_code]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_proje__proje__3493CFA7]') AND type = 'D')
BEGIN
ALTER TABLE [kob_project_user] ADD  DEFAULT (N'') FOR [project_name]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_proje__proje__3587F3E0]') AND type = 'D')
BEGIN
ALTER TABLE [kob_project_user] ADD  DEFAULT (N'') FOR [project_mode]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_proje__owner__367C1819]') AND type = 'D')
BEGIN
ALTER TABLE [kob_project_user] ADD  DEFAULT (NULL) FOR [owner]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_proje__versi__37703C52]') AND type = 'D')
BEGIN
ALTER TABLE [kob_project_user] ADD  DEFAULT ((0)) FOR [version]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_proje__gmt_c__3864608B]') AND type = 'D')
BEGIN
ALTER TABLE [kob_project_user] ADD  DEFAULT (getdate()) FOR [gmt_created]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_proje__gmt_m__395884C4]') AND type = 'D')
BEGIN
ALTER TABLE [kob_project_user] ADD  DEFAULT (getdate()) FOR [gmt_modified]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___proje__628FA481]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT ('') FOR [project_code]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___proje__6383C8BA]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT ('') FOR [project_name]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___job_u__6477ECF3]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT ('') FOR [job_uuid]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___job_t__656C112C]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT ('') FOR [job_type]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___job_c__66603565]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT ('') FOR [job_cn]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___task___6754599E]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT ('') FOR [task_key]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___task___68487DD7]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT ('') FOR [task_remark]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___task___693CA210]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT ('') FOR [task_type]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___task___6A30C649]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT ('') FOR [task_uuid]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___relat__6B24EA82]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT ('') FOR [relation_task_uuid]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___retry__6C190EBB]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT ('') FOR [retry_type]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task_r__rely__6D0D32F4]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT (NULL) FOR [rely]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___ances__6E01572D]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT (NULL) FOR [ancestor]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___cron___6EF57B66]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT ('') FOR [cron_expression]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___timeo__6FE99F9F]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT (NULL) FOR [timeout_threshold]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___failo__70DDC3D8]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT (NULL) FOR [failover]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___load___71D1E811]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT ('') FOR [load_balance]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___state__72C60C4A]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT (NULL) FOR [state]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___compl__73BA3083]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT (NULL) FOR [complete]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___retry__74AE54BC]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT (NULL) FOR [retry_count]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___batch__75A278F5]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT ('') FOR [batch_type]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___clien__76969D2E]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT ('') FOR [client_identification]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___trigg__778AC167]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT (NULL) FOR [trigger_time]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___consu__787EE5A0]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT (NULL) FOR [consumption_time]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___execu__797309D9]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT (NULL) FOR [execute_start_time]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___execu__7A672E12]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT (NULL) FOR [execute_end_time]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___versi__7B5B524B]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT ('0') FOR [version]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___gmt_c__7C4F7684]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT (getdate()) FOR [gmt_created]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___gmt_m__7D439ABD]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_record] ADD  DEFAULT (getdate()) FOR [gmt_modified]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___proje__7F2BE32F]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_waiting] ADD  DEFAULT ('') FOR [project_code]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___proje__00200768]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_waiting] ADD  DEFAULT ('') FOR [project_name]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___job_u__01142BA1]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_waiting] ADD  DEFAULT ('') FOR [job_uuid]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___job_t__02084FDA]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_waiting] ADD  DEFAULT ('') FOR [job_type]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___job_c__02FC7413]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_waiting] ADD  DEFAULT ('') FOR [job_cn]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___task___03F0984C]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_waiting] ADD  DEFAULT ('') FOR [task_key]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___task___04E4BC85]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_waiting] ADD  DEFAULT ('') FOR [task_remark]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___task___05D8E0BE]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_waiting] ADD  DEFAULT ('') FOR [task_type]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___task___06CD04F7]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_waiting] ADD  DEFAULT ('') FOR [task_uuid]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___relat__07C12930]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_waiting] ADD  DEFAULT ('') FOR [relation_task_uuid]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___retry__08B54D69]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_waiting] ADD  DEFAULT ('') FOR [retry_type]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___batch__09A971A2]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_waiting] ADD  DEFAULT ('') FOR [batch_type]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___failo__0A9D95DB]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_waiting] ADD  DEFAULT (NULL) FOR [failover]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___load___0B91BA14]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_waiting] ADD  DEFAULT ('') FOR [load_balance]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task_w__rely__0C85DE4D]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_waiting] ADD  DEFAULT (NULL) FOR [rely]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___cron___0D7A0286]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_waiting] ADD  DEFAULT ('') FOR [cron_expression]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___timeo__0E6E26BF]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_waiting] ADD  DEFAULT (NULL) FOR [timeout_threshold]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___retry__0F624AF8]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_waiting] ADD  DEFAULT (NULL) FOR [retry_count]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___trigg__10566F31]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_waiting] ADD  DEFAULT (NULL) FOR [trigger_time]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___versi__114A936A]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_waiting] ADD  DEFAULT ('0') FOR [version]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___gmt_c__123EB7A3]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_waiting] ADD  DEFAULT (getdate()) FOR [gmt_created]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_task___gmt_m__1332DBDC]') AND type = 'D')
BEGIN
ALTER TABLE [kob_task_waiting] ADD  DEFAULT (getdate()) FOR [gmt_modified]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_user_i__code__160F4887]') AND type = 'D')
BEGIN
ALTER TABLE [kob_user] ADD  DEFAULT (N'') FOR [code]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_user_i__name__17036CC0]') AND type = 'D')
BEGIN
ALTER TABLE [kob_user] ADD  DEFAULT (N'') FOR [name]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_user_in__pwd__17F790F9]') AND type = 'D')
BEGIN
ALTER TABLE [kob_user] ADD  DEFAULT (N'') FOR [pwd]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_user___versi__18EBB532]') AND type = 'D')
BEGIN
ALTER TABLE [kob_user] ADD  DEFAULT ((0)) FOR [version]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_user___gmt_c__19DFD96B]') AND type = 'D')
BEGIN
ALTER TABLE [kob_user] ADD  DEFAULT (getdate()) FOR [gmt_created]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[DF__kob_user___gmt_m__1AD3FDA4]') AND type = 'D')
BEGIN
ALTER TABLE [kob_user] ADD  DEFAULT (getdate()) FOR [gmt_modified]
END
GO

INSERT INTO kob_user(code, name, pwd, configuration) VALUES ('xiaoming', '小明', 'xiaoming', '{"mail":"xiaoming@ke.com"}');
INSERT INTO kob_user(code, name, pwd, configuration) VALUES ('xiaohong', '小红', 'xiaohong', '{"mail":"xiaohong@ke.com"}');

