import 'package:flutter/material.dart';
import 'package:cached_network_image/cached_network_image.dart';
import '../../core/theme/app_theme.dart';
import '../../data/models/story_model.dart';

/// 故事卡片组件
class StoryCard extends StatelessWidget {
  final Story story;
  final VoidCallback? onTap;
  final bool showFavorite;

  const StoryCard({
    super.key,
    required this.story,
    this.onTap,
    this.showFavorite = false,
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        decoration: BoxDecoration(
          color: AppColors.surface,
          borderRadius: BorderRadius.circular(12),
        ),
        child: Row(
          children: [
            // 封面图
            ClipRRect(
              borderRadius: const BorderRadius.horizontal(
                left: Radius.circular(12),
              ),
              child: story.coverUrl != null
                  ? CachedNetworkImage(
                      imageUrl: story.coverUrl!,
                      width: 100,
                      height: 100,
                      fit: BoxFit.cover,
                      placeholder: (_, __) => Container(
                        color: AppColors.surfaceLight,
                        child: const Center(
                          child: Icon(Icons.music_note, color: AppColors.textSecondary),
                        ),
                      ),
                      errorWidget: (_, __, ___) => Container(
                        color: AppColors.surfaceLight,
                        child: const Center(
                          child: Icon(Icons.music_note, color: AppColors.textSecondary),
                        ),
                      ),
                    )
                  : Container(
                      width: 100,
                      height: 100,
                      color: AppColors.surfaceLight,
                      child: const Center(
                        child: Icon(Icons.music_note, color: AppColors.textSecondary),
                      ),
                    ),
            ),
            // 内容
            Expanded(
              child: Padding(
                padding: const EdgeInsets.all(12),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      story.title,
                      style: Theme.of(context).textTheme.titleMedium,
                      maxLines: 2,
                      overflow: TextOverflow.ellipsis,
                    ),
                    const SizedBox(height: 4),
                    if (story.author != null)
                      Text(
                        story.author!,
                        style: Theme.of(context).textTheme.bodySmall,
                      ),
                    const SizedBox(height: 8),
                    Row(
                      children: [
                        Icon(
                          Icons.access_time,
                          size: 14,
                          color: AppColors.textSecondary,
                        ),
                        const SizedBox(width: 4),
                        Text(
                          story.formattedDuration,
                          style: Theme.of(context).textTheme.bodySmall,
                        ),
                        if (story.category != null) ...[
                          const SizedBox(width: 12),
                          Container(
                            padding: const EdgeInsets.symmetric(
                              horizontal: 8,
                              vertical: 2,
                            ),
                            decoration: BoxDecoration(
                              color: AppColors.primary.withValues(alpha: 0.2),
                              borderRadius: BorderRadius.circular(4),
                            ),
                            child: Text(
                              story.category!,
                              style: TextStyle(
                                fontSize: 10,
                                color: AppColors.primary,
                              ),
                            ),
                          ),
                        ],
                      ],
                    ),
                  ],
                ),
              ),
            ),
            // 收藏按钮
            if (showFavorite)
              IconButton(
                icon: const Icon(Icons.favorite_border),
                color: AppColors.textSecondary,
                onPressed: () {
                  // TODO: 收藏/取消收藏
                },
              ),
          ],
        ),
      ),
    );
  }
}
