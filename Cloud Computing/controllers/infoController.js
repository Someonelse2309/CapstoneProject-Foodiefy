// Controllers for about and terms APIs
exports.getAbout = (req, res) => {
    const aboutInfo = {
      description: "Aplikasi ini dirancang untuk membantu pengguna dalam mengelola kesehatan dan nutrisi mereka dengan fitur yang lengkap dan mudah digunakan."
    };
  
    res.status(200).json(aboutInfo);
  };
  
exports.getTerms = (req, res) => {
    const termsAndConditions = {
      text: "Syarat dan ketentuan ini mengatur penggunaan aplikasi kami. Dengan menggunakan aplikasi ini, Anda setuju untuk mematuhi syarat dan ketentuan yang berlaku."
    };
  
    res.status(200).json(termsAndConditions);
  };
  