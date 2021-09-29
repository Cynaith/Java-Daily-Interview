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
  
### ElasticSearch Api
- 创建索引
    - `PUT test`
- 查看索引
    - `GET test` 
        - aliases: 索引别名(一个索引别名可以映射多个真实索引)
        - mappings: 对索引库中索引的字段名称及其数据类型进行定义
        - setting: 索引配置
- 测试索引是否存在
    - `HEAD test`
- 删除索引
    - `DELETE test`

- 添加mapping
```http request
PUT test/_mapping
{
  "properties": {
    "title":{
          "type": "text"
        },
        "images":{
          "type": "text"
        },
        "price":{
          "type": "integer"
        }
  }
}
```    

- 添加数据
```http request
POST /test/_doc/{_id}
{
  "title" : "标题"
}
``` 
    - 可以在post后面置指定文档id

- 删除数据
    -  `DELETE test/_doc/{_id}
`
- 查询全部
```http request
GET test/_search
{
    "query": {
        "match_all":{}
    }
}
```
- 分词查询
```http request
GET test/_search
{
  "query": {
    "match": {
      "title":{
        "query": "小米手机电视"
      }
    }
  }
}


GET test/_search
{
  "query": {
   "multi_match": {
     "query": "小米出品",
     "fields": ["title","subTitle"]
   }
  }
}

```

- 范围查询(字段type = integer)
    - gte 大于或大于等于 gt:大于
    - lte 小于或小于等于 lt:小于
```http request
GET test/_search
{
  "query": {
    "range": {
      "price": {
        "gte": 1999, 
        "lte": 2999   
      }
    }
  }
}
```

- 过滤查询
```http request
GET test/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "title": "小米手机"
          }
        }
      ],"filter": [
        {
          "range": {
            "price": {
              "gte": 2000,
              "lte": 3000
            }
          }
        }
      ]
    }
  }
}
```

- 排序
```http request
GET test/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "title": "手机"
          }
        }
      ]
    }
  },"sort": [
    {
      "price": {
        "order": "desc"
      }
    }
  ]
}
```


### Text 与 Keyword的区别

- 为什么会出现text和keyword类型？
    - Elastic Search 5.x及以后版本取消了`string`类型，拆分为`text`和`keyword`  
    区别在于`text`会对字段进行分词处理，而`keyword`不会

    
- Text
    - 会分词，然后进行索引
    - 支持模糊、精确查询
    - 不支持聚合
    
- keyword
    - 不进行分词，直接索引
    - 支持模糊、精确查询
    - 支持聚合
    
- Dynamic Mapping
    - 在使用 ES 的时，我们不需要事先定义好映射设置就可以直接向索引中导入文档。ES 可以自动实现每个字段的类型检测，并进行 mapping 设置，这个过程就叫动态映射
    - 规则
        - 例如传入的文档中字段price的值为12，那么price将被映射为long类型
        - 字段addr的值为"192.168.0.1"，那么addr将被映射为ip类型
        - 对于其他字段(title)而言，ES会将它们映射为text类型，但为了保留对这些字段做精确查询以及聚合的能力，又同时对它们做了keyword类型的映射
        ```json
        {
         "foobar": {
          "type": "text",
          "fields": {
             "keyword": {
                 "type": "keyword",
                 "ignore_above": 256
              }
          }
         }
        }
         ```
      在之后的查询中使用title是将title作为text类型查询，而使用title.keyword则是将title作为keyword类型查询。前者会对查询内容做分词处理之后再匹配，而后者则是直接对查询结果做精确匹配。
      - ES的term query做的是精确匹配而不是分词查询，因此对text类型的字段做term查询将是查不到结果的（除非字段本身经过分词器处理后不变，未被转换或分词）。此时，必须使用 title.keyword来对title字段以keyword类型进行精确匹配。

### 聚合(Aggregations)
- 聚合的两个基础概念
    - 桶(Buckets)：符合条件的文档的集合，相当于SQL中的group by。比如，在users表中，按“地区”聚合，一个人将被分到北京桶或上海桶或其他桶里；按“性别”聚合，一个人将被分到男桶或女桶
    - 指标(Metrics)：基于Buckets的基础上进行统计分析，相当于SQL中的count,avg,sum等。比如，按“地区”聚合，计算每个地区的人数，平均年龄等
