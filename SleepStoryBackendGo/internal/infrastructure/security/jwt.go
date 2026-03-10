package security

import (
	"fmt"
	"time"

	"github.com/golang-jwt/jwt/v5"
)

// JWTConfig JWT配置
type JWTConfig struct {
	Secret     string
	Expiration time.Duration
}

// JWTClaims JWT声明
type JWTClaims struct {
	UserID string `json:"userId"`
	Phone  string `json:"phone"`
	jwt.RegisteredClaims
}

// JWTTokenProvider JWT令牌提供者
type JWTTokenProvider struct {
	config JWTConfig
}

// NewJWTTokenProvider 创建JWT提供者
func NewJWTTokenProvider(config JWTConfig) *JWTTokenProvider {
	return &JWTTokenProvider{config: config}
}

// GenerateToken 生成令牌
func (p *JWTTokenProvider) GenerateToken(userID, phone string) (string, error) {
	now := time.Now()
	claims := JWTClaims{
		UserID: userID,
		Phone:  phone,
		RegisteredClaims: jwt.RegisteredClaims{
			Subject:   userID,
			IssuedAt:  jwt.NewNumericDate(now),
			ExpiresAt: jwt.NewNumericDate(now.Add(p.config.Expiration)),
		},
	}

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	return token.SignedString([]byte(p.config.Secret))
}

// ValidateToken 验证令牌
func (p *JWTTokenProvider) ValidateToken(tokenString string) (*JWTClaims, error) {
	token, err := jwt.ParseWithClaims(tokenString, &JWTClaims{}, func(token *jwt.Token) (interface{}, error) {
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
		}
		return []byte(p.config.Secret), nil
	})

	if err != nil {
		return nil, err
	}

	if claims, ok := token.Claims.(*JWTClaims); ok && token.Valid {
		return claims, nil
	}

	return nil, fmt.Errorf("invalid token")
}

// GetUserIDFromToken 从令牌获取用户ID
func (p *JWTTokenProvider) GetUserIDFromToken(tokenString string) (string, error) {
	claims, err := p.ValidateToken(tokenString)
	if err != nil {
		return "", err
	}
	return claims.UserID, nil
}
