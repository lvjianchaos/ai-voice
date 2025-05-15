import { onMounted } from 'vue'

/**
 * 动画效果组合式API
 * 提供网站全局动画和交互效果
 */
export function useAnimations() {
  // 鼠标悬浮动画效果
  const applyHoverEffects = () => {
    // 按钮悬浮效果
    const buttons = document.querySelectorAll('.el-button:not(.no-hover-effect)')
    buttons.forEach((button) => {
      button.addEventListener('mouseenter', (e) => {
        const target = e.currentTarget as HTMLElement
        target.style.transition = 'all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1)'

        // 不是禁用状态才添加效果
        if (!target.classList.contains('is-disabled')) {
          target.style.transform = 'translateY(-5px)'
          target.style.boxShadow = '0 10px 20px rgba(0, 0, 0, 0.1)'
        }
      })

      button.addEventListener('mouseleave', (e) => {
        const target = e.currentTarget as HTMLElement
        target.style.transform = 'translateY(0)'
        target.style.boxShadow = 'none'
      })
    })

    // 卡片悬浮效果
    const cards = document.querySelectorAll('.el-card:not(.no-hover-effect)')
    cards.forEach((card) => {
      card.addEventListener('mouseenter', (e) => {
        const target = e.currentTarget as HTMLElement
        target.style.transition = 'all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1)'
        target.style.transform = 'translateY(-8px)'
        target.style.boxShadow = 'var(--shadow-lg)'
      })

      card.addEventListener('mouseleave', (e) => {
        const target = e.currentTarget as HTMLElement
        target.style.transform = 'translateY(0)'
        target.style.boxShadow = 'var(--shadow-md)'
      })
    })
  }

  // 滚动动画效果
  const applyScrollEffects = () => {
    // 滚动进入视口时的动画效果
    const elementsToAnimate = document.querySelectorAll('.animate-on-scroll')

    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add('visible')
            observer.unobserve(entry.target)
          }
        })
      },
      {
        threshold: 0.1,
      },
    )

    elementsToAnimate.forEach((element) => {
      observer.observe(element)
      element.classList.add('opacity-0', 'transition-all', 'duration-700')
    })
  }

  // 鼠标跟随效果
  const applyMouseFollowEffect = () => {
    const cursor = document.createElement('div')
    cursor.classList.add('custom-cursor')
    document.body.appendChild(cursor)

    const cursorInner = document.createElement('div')
    cursorInner.classList.add('cursor-inner')
    cursor.appendChild(cursorInner)

    document.addEventListener('mousemove', (e) => {
      cursor.style.transform = `translate(${e.clientX}px, ${e.clientY}px)`
    })

    // 互动区域的特殊效果
    const interactiveElements = document.querySelectorAll('a, button, .el-button, .interactive')
    interactiveElements.forEach((el) => {
      el.addEventListener('mouseenter', () => {
        cursor.classList.add('cursor-hover')
      })

      el.addEventListener('mouseleave', () => {
        cursor.classList.remove('cursor-hover')
      })
    })
  }

  // 添加打字机效果
  const applyTypingEffect = (selector: string, text: string, speed = 100) => {
    const element = document.querySelector(selector) as HTMLElement
    if (!element) return

    const originalText = text || element.innerText
    element.innerText = ''

    let i = 0
    const typing = setInterval(() => {
      if (i < originalText.length) {
        element.innerText += originalText.charAt(i)
        i++
      } else {
        clearInterval(typing)
      }
    }, speed)
  }

  // 添加数字递增效果
  const applyCountUpEffect = (selector: string, endValue: number, duration = 2000) => {
    const element = document.querySelector(selector) as HTMLElement
    if (!element) return

    const startTime = performance.now()
    const startValue = 0

    const updateCounter = (timestamp: number) => {
      const elapsedTime = timestamp - startTime

      if (elapsedTime < duration) {
        const progress = elapsedTime / duration
        const currentValue = Math.round(startValue + progress * (endValue - startValue))
        element.innerText = currentValue.toString()
        requestAnimationFrame(updateCounter)
      } else {
        element.innerText = endValue.toString()
      }
    }

    requestAnimationFrame(updateCounter)
  }

  // 添加视差滚动效果
  const applyParallaxEffect = () => {
    const parallaxElements = document.querySelectorAll('.parallax')

    window.addEventListener('scroll', () => {
      const scrollTop = window.pageYOffset

      parallaxElements.forEach((element) => {
        const el = element as HTMLElement
        const speed = Number(el.dataset.speed || 0.5)
        el.style.transform = `translateY(${scrollTop * speed}px)`
      })
    })
  }

  // 初始化所有动画效果
  const initializeAnimations = () => {
    applyHoverEffects()
    applyScrollEffects()
    // 以下效果可根据需要启用
    // applyMouseFollowEffect();
    applyParallaxEffect()

    // 添加全局CSS类
    const style = document.createElement('style')
    style.innerHTML = `
      /* 滚动动画基本样式 */
      .opacity-0 { opacity: 0; }
      .transition-all { transition: all 0.7s ease-out; }
      .duration-700 { transition-duration: 700ms; }
      .visible { opacity: 1; transform: translateY(0) !important; }

      /* 从下方进入动画 */
      .slide-up-animation { transform: translateY(30px); }

      /* 从左侧进入动画 */
      .slide-right-animation { transform: translateX(-30px); }

      /* 从右侧进入动画 */
      .slide-left-animation { transform: translateX(30px); }

      /* 放大进入动画 */
      .scale-animation { transform: scale(0.9); }

      /* 鼠标跟随效果 */
      .custom-cursor {
        position: fixed;
        width: 40px;
        height: 40px;
        border: 2px solid var(--primary-color);
        border-radius: 50%;
        pointer-events: none;
        transform: translate(-50%, -50%);
        z-index: 9999;
        transition: all 0.1s ease;
        opacity: 0.6;
      }

      .cursor-inner {
        position: absolute;
        width: 8px;
        height: 8px;
        background-color: var(--primary-color);
        border-radius: 50%;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        transition: all 0.2s ease;
      }

      .cursor-hover {
        transform: translate(-50%, -50%) scale(1.5);
        opacity: 0.8;
      }

      .cursor-hover .cursor-inner {
        transform: translate(-50%, -50%) scale(0.5);
      }

      /* 视差滚动效果 */
      .parallax {
        will-change: transform;
        overflow: hidden;
      }
    `
    document.head.appendChild(style)
  }

  // 在组件挂载后初始化动画
  onMounted(() => {
    initializeAnimations()
  })

  return {
    applyTypingEffect,
    applyCountUpEffect,
    initializeAnimations,
  }
}
