import '../../data/models/community_model.dart';

/// 社区仓储接口
abstract class CommunityRepository {
  /// 获取社区动态流
  Future<List<CommunityStory>> getFeed({int page = 1, int size = 20});

  /// 获取故事详情
  Future<CommunityStory> getStoryDetail(String id);

  /// 点赞/取消点赞
  Future<void> toggleLike(String storyId);

  /// 获取用户发布的故事
  Future<List<CommunityStory>> getUserStories(String userId, {int page = 1, int size = 20});

  /// 发布故事
  Future<CommunityStory> publishStory(PublishStoryRequest request);

  /// 删除故事
  Future<void> deleteStory(String storyId);
}
