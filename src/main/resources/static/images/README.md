## Image Directory

This directory contains static images served by the application.

### Required Files:
- **placeholder.png**: Default image shown when product.imageUrl is empty or null
  - Recommended size: 300x300px
  - Should contain "No Image Available" or similar placeholder text

### Directory Structure:
- `/images/products/` - Product images
- `/images/categories/` - Category images
- `/images/placeholder.png` - Fallback image

### Usage:
Images are referenced in templates using relative paths like:
- `/images/products/product1.jpg`
- `/images/categories/electronics.png`
- `/images/placeholder.png` (fallback)

### Note:
You'll need to add actual image files to this directory. The templates expect a placeholder.png file for products without images.