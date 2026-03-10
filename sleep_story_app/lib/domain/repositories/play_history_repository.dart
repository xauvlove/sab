import '../../data/models/play_history_model.dart';

/// 播放历史仓储接口
abstract class PlayHistoryRepository {
  /// 获取播放历史列表
  Future<List<PlayHistoryModel>> getPlayHistory({int limit = 20, int offset = 0});

  /// 记录播放历史
  Future<void> recordPlayHistory({
    required String storyId,
    required int durationListened,
  });

  /// 删除单条播放历史
  Future<void> deletePlayHistory(String historyId);

  /// 清空所有播放历史
  Future<void> clearPlayHistory();
}
