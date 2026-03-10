import '../../data/models/story_model.dart';

/// 故事仓储接口
abstract class StoryRepository {
  /// 获取首页数据
  Future<HomeData> getHomeData();

  /// 获取故事详情
  Future<Story> getStoryDetail(String id);

  /// 获取分类列表
  Future<List<Category>> getCategories();

  /// 根据分类获取故事列表
  Future<List<Story>> getStoriesByCategory(String categoryId, {int page = 1, int size = 20});

  /// 搜索故事
  Future<List<Story>> searchStories(String keyword, {int page = 1, int size = 20});

  /// 获取推荐故事
  Future<List<Story>> getRecommendedStories({int size = 10});
}
