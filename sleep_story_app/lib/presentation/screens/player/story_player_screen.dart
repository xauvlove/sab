import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:cached_network_image/cached_network_image.dart';
import '../../../core/theme/app_theme.dart';
import '../../../data/models/story_model.dart';
import '../../providers/player_provider.dart';
import '../../providers/favorite_provider.dart';

/// 故事播放页面
class StoryPlayerScreen extends StatefulWidget {
  final Story story;

  const StoryPlayerScreen({super.key, required this.story});

  @override
  State<StoryPlayerScreen> createState() => _StoryPlayerScreenState();
}

class _StoryPlayerScreenState extends State<StoryPlayerScreen> {
  bool _isPlaying = false;
  double _progress = 0.0;
  int _currentPosition = 0;
  int _totalDuration = 0;
  int _sleepTimerMinutes = 0;

  @override
  void initState() {
    super.initState();
    _totalDuration = widget.story.duration;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: [AppColors.surface, AppColors.background],
          ),
        ),
        child: SafeArea(
          child: Column(
            children: [
              // 顶部导航
              _buildAppBar(),
              const Spacer(),
              // 封面图
              _buildCover(),
              const SizedBox(height: 32),
              // 故事信息
              _buildStoryInfo(),
              const SizedBox(height: 32),
              // 播放进度
              _buildProgress(),
              const SizedBox(height: 24),
              // 播放控制
              _buildControls(),
              const SizedBox(height: 24),
              // 睡眠定时
              _buildSleepTimer(),
              const Spacer(),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildAppBar() {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 8),
      child: Row(
        children: [
          IconButton(
            icon: const Icon(Icons.keyboard_arrow_down, size: 32),
            onPressed: () => Navigator.pop(context),
          ),
          const Spacer(),
          Consumer<FavoriteProvider>(
            builder: (context, fav, _) {
              final isFav = fav.isFavorite(widget.story.id);
              return IconButton(
                icon: Icon(
                  isFav ? Icons.favorite : Icons.favorite_border,
                  color: isFav ? AppColors.error : null,
                ),
                onPressed: () => fav.toggleFavorite(widget.story.id),
              );
            },
          ),
          IconButton(
            icon: const Icon(Icons.more_vert),
            onPressed: () {},
          ),
        ],
      ),
    );
  }

  Widget _buildCover() {
    return Container(
      width: 280,
      height: 280,
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: AppColors.primary.withValues(alpha: 0.3),
            blurRadius: 30,
            offset: const Offset(0, 10),
          ),
        ],
      ),
      child: ClipRRect(
        borderRadius: BorderRadius.circular(16),
        child: widget.story.coverUrl != null
            ? CachedNetworkImage(
                imageUrl: widget.story.coverUrl!,
                fit: BoxFit.cover,
                placeholder: (_, __) => Container(
                  color: AppColors.surfaceLight,
                  child: const Icon(Icons.music_note, size: 80),
                ),
              )
            : Container(
                color: AppColors.surfaceLight,
                child: const Icon(Icons.music_note, size: 80),
              ),
      ),
    );
  }

  Widget _buildStoryInfo() {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 32),
      child: Column(
        children: [
          Text(
            widget.story.title,
            style: Theme.of(context).textTheme.headlineSmall,
            textAlign: TextAlign.center,
            maxLines: 2,
            overflow: TextOverflow.ellipsis,
          ),
          const SizedBox(height: 8),
          if (widget.story.author != null)
            Text(
              widget.story.author!,
              style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                    color: AppColors.textSecondary,
                  ),
            ),
          if (widget.story.category != null) ...[
            const SizedBox(height: 8),
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 4),
              decoration: BoxDecoration(
                color: AppColors.primary.withValues(alpha: 0.2),
                borderRadius: BorderRadius.circular(12),
              ),
              child: Text(
                widget.story.category!,
                style: const TextStyle(color: AppColors.primary, fontSize: 12),
              ),
            ),
          ],
        ],
      ),
    );
  }

  Widget _buildProgress() {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 32),
      child: Column(
        children: [
          SliderTheme(
            data: SliderTheme.of(context).copyWith(
              trackHeight: 4,
              thumbShape: const RoundSliderThumbShape(enabledThumbRadius: 6),
              overlayShape: const RoundSliderOverlayShape(overlayRadius: 14),
            ),
            child: Slider(
              value: _progress,
              onChanged: (value) {
                setState(() {
                  _progress = value;
                  _currentPosition = (value * _totalDuration).toInt();
                });
              },
              activeColor: AppColors.primary,
              inactiveColor: AppColors.surfaceLight,
            ),
          ),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 8),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  _formatDuration(_currentPosition),
                  style: Theme.of(context).textTheme.bodySmall,
                ),
                Text(
                  _formatDuration(_totalDuration),
                  style: Theme.of(context).textTheme.bodySmall,
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildControls() {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        // 后退10秒
        IconButton(
          icon: const Icon(Icons.replay_10),
          iconSize: 36,
          onPressed: () {
            setState(() {
              _currentPosition = (_currentPosition - 10).clamp(0, _totalDuration);
              _progress = _currentPosition / _totalDuration;
            });
          },
        ),
        const SizedBox(width: 16),
        // 播放/暂停
        Consumer<PlayerProvider>(
          builder: (context, player, _) {
            final isPlaying = player.currentStoryId == widget.story.id &&
                player.status == PlayerStatus.playing;
            return GestureDetector(
              onTap: () {
                if (isPlaying) {
                  player.pause();
                } else {
                  // TODO: 播放音频
                  player.playText(widget.story.id, '播放故事: ${widget.story.title}');
                }
                setState(() => _isPlaying = !_isPlaying);
              },
              child: Container(
                width: 72,
                height: 72,
                decoration: const BoxDecoration(
                  color: AppColors.primary,
                  shape: BoxShape.circle,
                ),
                child: Icon(
                  isPlaying ? Icons.pause : Icons.play_arrow,
                  size: 40,
                  color: Colors.white,
                ),
              ),
            );
          },
        ),
        const SizedBox(width: 16),
        // 前进10秒
        IconButton(
          icon: const Icon(Icons.forward_10),
          iconSize: 36,
          onPressed: () {
            setState(() {
              _currentPosition = (_currentPosition + 10).clamp(0, _totalDuration);
              _progress = _currentPosition / _totalDuration;
            });
          },
        ),
      ],
    );
  }

  Widget _buildSleepTimer() {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Icon(
          Icons.bedtime_outlined,
          color: _sleepTimerMinutes > 0 ? AppColors.primary : AppColors.textSecondary,
        ),
        const SizedBox(width: 8),
        PopupMenuButton<int>(
          initialValue: _sleepTimerMinutes,
          onSelected: (value) {
            setState(() => _sleepTimerMinutes = value);
          },
          itemBuilder: (context) => [
            const PopupMenuItem(value: 0, child: Text('关闭定时')),
            const PopupMenuItem(value: 15, child: Text('15分钟后')),
            const PopupMenuItem(value: 30, child: Text('30分钟后')),
            const PopupMenuItem(value: 45, child: Text('45分钟后')),
            const PopupMenuItem(value: 60, child: Text('60分钟后')),
          ],
          child: Text(
            _sleepTimerMinutes > 0 ? '$_sleepTimerMinutes分钟后停止' : '睡眠定时',
            style: TextStyle(
              color: _sleepTimerMinutes > 0 ? AppColors.primary : AppColors.textSecondary,
            ),
          ),
        ),
      ],
    );
  }

  String _formatDuration(int seconds) {
    final minutes = seconds ~/ 60;
    final secs = seconds % 60;
    return '${minutes.toString().padLeft(2, '0')}:${secs.toString().padLeft(2, '0')}';
  }
}
