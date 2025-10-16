import React, { useEffect, useState, useCallback } from 'react'
import { useNavigate } from 'react-router-dom'
import { helpingToolsAPI } from '../utils/api'
import '../styles/HelpingTools.css'

const HelpingTools = ({ userId, onLogout }) => {
  const navigate = useNavigate()
  const [youtubeLinks, setYoutubeLinks] = useState([])
  const [equipmentLinks, setEquipmentLinks] = useState([])
  const [newYoutubeLink, setNewYoutubeLink] = useState('')
  const [newEquipmentLink, setNewEquipmentLink] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [tools, setTools] = useState([])

  const fetchHelpingTools = useCallback(async () => {
    try {
      setLoading(true)
      const [youtubeResponse, equipmentResponse] = await Promise.all([
        helpingToolsAPI.getYouTubeLinks(userId),
        helpingToolsAPI.getEquipmentLinks(userId),
      ])

      setYoutubeLinks(youtubeResponse)
      setEquipmentLinks(equipmentResponse)

      const res = await fetch('/api/helping-tools')
      const data = await res.json()
      setTools(data)
    } catch (error) {
      console.error('Error fetching helping tools:', error)
      setError('Failed to load helping tools')
    } finally {
      setLoading(false)
    }
  }, [userId])

  useEffect(() => {
    fetchHelpingTools()
  }, [fetchHelpingTools])

  const validateYouTubeUrl = (url) => {
    return url.toLowerCase().includes('youtube.com') || url.toLowerCase().includes('youtu.be')
  }

  const validateUrl = (url) => {
    try {
      new URL(url)
      return true
    } catch {
      return false
    }
  }

  const handleAddYoutubeLink = async () => {
    if (!newYoutubeLink.trim()) {
      setError('Please enter a YouTube link')
      return
    }

    if (!validateUrl(newYoutubeLink)) {
      setError('Please enter a valid URL')
      return
    }

    if (!validateYouTubeUrl(newYoutubeLink)) {
      setError('Please enter a valid YouTube URL')
      return
    }

    try {
      setError('')
      setSuccess('')

      await helpingToolsAPI.addYouTubeLink(userId, newYoutubeLink, 'YouTube Video')
      setNewYoutubeLink('')
      setSuccess('YouTube link added successfully!')
      await fetchHelpingTools()
    } catch (error) {
      console.error('Error adding YouTube link:', error)
      setError(error.response?.data?.message || 'Error adding YouTube link')
    }
  }

  const handleAddEquipmentLink = async () => {
    if (!newEquipmentLink.trim()) {
      setError('Please enter an equipment link')
      return
    }

    if (!validateUrl(newEquipmentLink)) {
      setError('Please enter a valid URL')
      return
    }

    try {
      setError('')
      setSuccess('')

      await helpingToolsAPI.addEquipmentLink(userId, newEquipmentLink, 'Equipment Resource')
      setNewEquipmentLink('')
      setSuccess('Equipment link added successfully!')
      await fetchHelpingTools()
    } catch (error) {
      console.error('Error adding equipment link:', error)
      setError(error.response?.data?.message || 'Error adding equipment link')
    }
  }

  const handleDeleteLink = async (id, type) => {
    if (window.confirm(`Are you sure you want to delete this ${type} link?`)) {
      try {
        await helpingToolsAPI.deleteHelpingTool(id)
        setSuccess(`${type} link deleted successfully!`)
        await fetchHelpingTools()
      } catch (error) {
        console.error('Error deleting link:', error)
        setError('Error deleting link')
      }
    }
  }

  const handleLogout = () => {
    onLogout()
    navigate('/')
  }

  const handleNavigation = (path) => {
    navigate(path)
  }

  const clearMessages = () => {
    setError('')
    setSuccess('')
  }

  return (
    <div className="helping-tools-page fade-in">
      <div className="sidebar slide-in-left">
        <nav className="sidebar-nav">
          <div
            className="nav-item"
            onClick={() => handleNavigation('/dashboard')}
          >
            Dash Board
          </div>
          <div
            className="nav-item"
            onClick={() => handleNavigation('/user-details')}
          >
            User Details
          </div>
          <div
            className="nav-item"
            onClick={() => handleNavigation('/workout-plan')}
          >
            Work Out Plan
          </div>
          <div className="nav-item active">Helping tools</div>
          <div className="nav-item logout" onClick={handleLogout}>
            Log Out
          </div>
        </nav>
      </div>

      <div className="main-content">
        {error && <div className="error-message">{error}</div>}
        {success && <div className="success-message">{success}</div>}

        {loading && <div className="loading">Loading helping tools...</div>}

        <div className="tools-section">
          <div className="youtube-section">
            <h2>Youtube Links</h2>
            <div className="links-container">
              {youtubeLinks.length > 0 ? (
                youtubeLinks.map((link) => (
                  <div key={link.id} className="link-item">
                    <div className="link-content">
                      <a
                        href={link.url}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="link-url"
                      >
                        {link.url}
                      </a>
                      {link.description && (
                        <span className="link-description">{link.description}</span>
                      )}
                    </div>
                    <button
                      className="delete-link-btn"
                      onClick={() => handleDeleteLink(link.id, 'YouTube')}
                    >
                      ×
                    </button>
                  </div>
                ))
              ) : (
                <div className="no-links">No YouTube links added yet</div>
              )}

              <div className="add-link">
                <input
                  type="url"
                  value={newYoutubeLink}
                  onChange={(e) => {
                    setNewYoutubeLink(e.target.value)
                    clearMessages()
                  }}
                  placeholder="Enter YouTube link... (e.g., https://youtube.com/watch?v=...)"
                  onKeyPress={(e) => {
                    if (e.key === 'Enter') {
                      handleAddYoutubeLink()
                    }
                  }}
                />
              </div>
            </div>
          </div>

          {/* Equipment Links Section */}
          <div className="equipment-section">
            <h2>Equipment</h2>
            <div className="links-container">
              {equipmentLinks.length > 0 ? (
                equipmentLinks.map((link) => (
                  <div key={link.id} className="link-item">
                    <div className="link-content">
                      <a
                        href={link.url}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="link-url"
                      >
                        {link.url}
                      </a>
                      {link.description && (
                        <span className="link-description">{link.description}</span>
                      )}
                    </div>
                    <button
                      className="delete-link-btn"
                      onClick={() => handleDeleteLink(link.id, 'Equipment')}
                    >
                      ×
                    </button>
                  </div>
                ))
              ) : (
                <div className="no-links">No equipment links added yet</div>
              )}

              <div className="add-link">
                <input
                  type="url"
                  value={newEquipmentLink}
                  onChange={(e) => {
                    setNewEquipmentLink(e.target.value)
                    clearMessages()
                  }}
                  placeholder="Enter equipment link... (e.g., https://amazon.com/...)"
                  onKeyPress={(e) => {
                    if (e.key === 'Enter') {
                      handleAddEquipmentLink()
                    }
                  }}
                />
              </div>
            </div>
          </div>
        </div>

        {/* Summary Stats */}
        <div className="tools-summary">
          <div className="summary-item">
            <span className="summary-label">YouTube Links:</span>
            <span className="summary-count">{youtubeLinks.length}</span>
          </div>
          <div className="summary-item">
            <span className="summary-label">Equipment Links:</span>
            <span className="summary-count">{equipmentLinks.length}</span>
          </div>
          <div className="summary-item">
            <span className="summary-label">Total Resources:</span>
            <span className="summary-count">{youtubeLinks.length + equipmentLinks.length}</span>
          </div>
        </div>
      </div>
    </div>
  )
}

export default HelpingTools