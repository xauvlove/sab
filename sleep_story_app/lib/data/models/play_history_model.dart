/// 播放历史模型
class PlayHistoryModel {
  final String id;
  final String storyId;
  final String? storyTitle;
  final String? storyCoverUrl;
  final int? storyDuration;
  final String? storyAuthor;
  final int durationListened;
  final DateTime playedAt;

  PlayHistoryModel({
    required this.id,
    required this.storyId,
    this.storyTitle,
    this.storyCoverUrl,
    this.storyDuration,
    this.storyAuthor,
    required this.durationListened,
    required this.playedAt,
  });

  factory PlayHistoryModel.fromJson(Map<String, dynamic> json) {
    return PlayHistoryModel(
      id: json['id']?.toString() ?? '',
      storyId: json['storyId']?.toString() ?? json['story_id']?.toString() ?? '',
      storyTitle: json['storyTitle'] ?? json['title'],
      storyCoverUrl: json['storyCoverUrl'] ?? json['coverUrl'],
      storyDuration: json['storyDuration'] ?? json['duration'],
      storyAuthor: json['storyAuthor'] ?? json['author'],
      durationListened: json['duration'] as int? ?? json['durationListened'] as int? ?? 0,
      playedAt: json['playedAt'] != null
          ? DateTime.tryParse(json['playedAt'].toString()) ?? DateTime.now()
          : DateTime.now(),
    );
  }

  /// 格式化收听时长
  String get formattedDuration {
    final minutes = durationListened ~/ 60;
    final seconds = durationListened % 60;
    return '${minutes.toString().padLeft(2, '0')}:${seconds.toString().padLeft(2, '0')}';
  }

  /// 格式化播放时间
  String get formattedPlayTime {
    final now = DateTime.now();
    final difference = now.difference(playedAt);

    if (difference.inDays > 7) {
      return '${playedAt.month}月${playedAt.day}日';
    } else if (difference.inDays > 0) {
      return '${difference.inDays}天前';
    } else if (difference.inHours > 0) {
      return '${difference.inHours}小时前';
    } else if (difference.inMinutes > 0) {
      return '${difference.inMinutes}分钟前';
    } else {
      return '刚刚';
    }
  }

  /// 获取收听进度百分比
  double get progress {
    if (storyDuration == null || storyDuration == 0) return 0;
    return (durationListened / storyDuration!).clamp(0.0, 1.0);
  }
}
