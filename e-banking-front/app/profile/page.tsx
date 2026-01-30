"use client"

import { useEffect, useState } from "react"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Button } from "@/components/ui/button"

interface Profile {
  sub: string;
  email_verified: boolean;
  name: string;
  phone_number: string;
  given_name: string;
  uuid: string;
  family_name: string;
  email: string;
  picture: string;
  conver_image: string;
  authorities: string[];
  username: string;
  gender?: string;
  dateOfBirth?: string;
}

export default function ProfilePage() {
  const [profile, setProfile] = useState<Profile | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetch('/api/v1/profile', {
      credentials: 'include'
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to fetch profile');
        }
        return response.json();
      })
      .then(data => {
        setProfile(data);
        setLoading(false);
        console.log('Fetched profile data:', data);
      })
      .catch(error => {
        console.error('Error fetching profile data:', error);
        setError(error.message);
        setLoading(false);
      });
  }, []);

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <p className="text-gray-600">Loading...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <p className="text-red-600">Error: {error}</p>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-b from-blue-50 to-white">
      {profile ? (
        <div className="max-w-2xl mx-auto pt-8 px-4">
          {/* Profile Card */}
          <div className="bg-white rounded-lg shadow-sm overflow-hidden">
            {/* Header Background / Cover Image */}
            <div className="h-32 bg-gradient-to-r from-blue-100 to-blue-50 relative">
              {profile.conver_image && (
                <img 
                  src={profile.conver_image} 
                  alt="Cover" 
                  className="w-full h-full object-cover"
                />
              )}
            </div>
            
            {/* Profile Content */}
            <div className="px-8 pb-8">
              {/* Avatar */}
              <div className="relative -mt-16 mb-4">
                <Avatar className="w-32 h-32 border-4 border-white shadow-lg">
                  <AvatarImage src={profile.picture} alt={profile.name || "Profile"} />
                  <AvatarFallback className="bg-slate-300 text-white text-3xl">
                    ?
                  </AvatarFallback>
                </Avatar>
              </div>

              {/* Name */}
              <h1 className="text-2xl font-semibold text-gray-900 mb-1">
                {profile.name || "—"}
              </h1>

              {/* Username */}
              <p className="text-gray-600 mb-1">
                @{profile.username}
              </p>

              {/* Email */}
              <p className="text-gray-600 mb-6">
                {profile.email}
              </p>

              {/* Edit Profile Button */}
              <Button className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg mb-8">
                Edit profile
              </Button>

              {/* Profile Details Grid */}
              <div className="grid grid-cols-2 gap-6">
                {/* Phone */}
                <div>
                  <p className="text-gray-500 text-sm mb-1">Phone</p>
                  <p className="text-gray-900 font-medium">
                    {profile.phone_number || "—"}
                  </p>
                </div>

                {/* Gender */}
                <div>
                  <p className="text-gray-500 text-sm mb-1">Gender</p>
                  <p className="text-gray-900 font-medium">
                    {profile.gender || "Male"}
                  </p>
                </div>

                {/* Date of Birth */}
                <div>
                  <p className="text-gray-500 text-sm mb-1">Date of Birth</p>
                  <p className="text-gray-900 font-medium">
                    {profile.dateOfBirth || "—"}
                  </p>
                </div>

                {/* UUID */}
                <div>
                  <p className="text-gray-500 text-sm mb-1">UUID</p>
                  <p className="text-gray-900 font-medium">
                    {profile.uuid || "—"}
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      ) : (
        <div className="min-h-screen flex items-center justify-center">
          <p className="text-gray-600">Not authenticated</p>
        </div>
      )}
    </div>
  );
}