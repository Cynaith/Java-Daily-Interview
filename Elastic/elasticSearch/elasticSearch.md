## elasticSearch

### 什么是elasticSearch
ElasticSearch 是一个分布式的免费开源搜索和分析引擎

### elasticSearch能做什么
- 应用程序搜索
- 网站搜索
- 企业搜索
- 日志处理和分析
- 基础设施指标和容器监测
- 地理空间数据分析和可视化
- 安全分析
- 业务分析

### 为何使用elasticSearch
- 响应时间快
  > ElasticSearch基于倒排索引
  >>  倒排索引: 根据关键词映射文档id
  > 
  >>  正向索引: 根据文档id映射关键词
- 可以分词搜索
  > ElasticSearch在搜索字段时会使用分词器进行分词
  >> Standard Analyzer(默认)(内置)  
  >> - 基于语法的标记化（基于Unicode文本分割算法）
  > 
  >> Simple Analyzer(内置)(只将英文大写转小写)
  > 
  >> Whitespace Analyzer(内置)(只按空格分词)
- 相关性搜索
  > ElasticSearch相关性打分机制
  >> 默认使用Lucene的TF-IDF技术
- 可视化
  > kibana

### ElasticSearch的结构
- 存储结构  
  Index(索引) -> Type(类型) -> Document(文档) -> Field(字段)
  > 由于在es内部，会将所有field合并，某个type中没有的field会用空值代替，所以会导致如下问题
  > 
  >> - 不同type里的字段需要保持一致
  >> 
  >> - 在某个type中没有存在的字段，也会消耗资源
  >
  > -  注：
  >   - 在ElasticSearch6.x版本中只允许一个索引下有一个type。
  >   - [ElasticSearch7.x版本使用默认_doc作为type](https://www.elastic.co/guide/en/elasticsearch/reference/current/removal-of-types.html)
  > 
  